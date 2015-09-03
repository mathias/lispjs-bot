(ns lispjs-bot.core
  (:require [cljs.nodejs :as node]))

(node/enable-util-print!)

(def irc (node/require "irc"))

;; ENV vars
(def env
  (.-env js/process))

(def irc-server (or (.-IRC_SERVER env)
                    "irc.freenode.org"))
(def username (or (.-USERNAME env)
                  "lispjs"))
(def password (.-PASSWORD env))
(def channel-list (let [chan-str (.-CHANNEL_LIST env)]
                    (.split chan-str ",")))

;; IRC client

(def Client (.-Client irc))
(def client (Client. irc-server
                     username
                     #js { :channels channel-list }))

;; IRC event handling
(defn add-listener [event callback]
  (.addListener client event callback))

(defn logger [ctx msg]
  (println ctx ": " msg))

(add-listener "error"
              (fn [msg]
                (logger "error" msg)))


(add-listener "message"
              (fn [from room message]
                (logger (str room " | " from) message)

                (when (and (re-seq #"^[#]" room)
                           (re-seq #"^!lispjs" message))
                  ;; Room notified, not PM
                  (cond
                   (re-seq #"dance" message) (.say client room "\u0001ACTION dances: :D\\-<\u0001")))))

(add-listener "motd"
              (fn [msg]
                ;; msg nickserv to log in
                (logger "motd" msg)

                (.say client
                      "NickServ"
                      (str "IDENTIFY " username " " password))
                (.whois client username
                        (fn [response]
                          (logger "whois" response)))))

(add-listener "invite"
              (fn [channel by]
                (logger "invite" (str "Invited to " channel " by " by))
                (if (some #{channel} channel-list)
                  (.join client channel)
                  (logger "Warning" (str "Invited to channel " channel " by " by " but " channel " is not in channel list!")))))

(add-listener "notice"
              (fn [from to msg]
                (logger (str (or from "") "> " to) msg)))

(add-listener "topic"
              (fn [channel topic nick]
                (logger "topic" (str channel "> " topic))))

(defn -main [& args]
  (println "Starting up lispjs"))

(set! *main-cli-fn* -main)
