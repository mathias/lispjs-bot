(ns lispjs-bot.core
  (:require [cljs.nodejs :as node]))

(node/enable-util-print!)

(def irc (node/require "irc"))

;; ENV vars
(defn env []
  (.-env js/process))

(def irc-server (or (.-IRC_SERVER (env))
                    "irc.freenode.org"))
(def username (or (.-USERNAME (env))
                  "lispjs"))
(def password (.-PASSWORD (env)))
(def channel-list (let [chan-str (.-CHANNEL_LIST (env))]
                    (.split chan-str ",")))

;; IRC client

(def Client (.-Client irc))
(def client (Client. irc-server
                     username
                     #js { :channels channel-list }))

;; IRC event handling
(defn add-listener [event callback]
  (.addListener client event callback))

(add-listener "error"
              (fn [msg]
                (println "error: " msg)))


(add-listener "message"
              (fn [from room message]
                (println room " | " from ":" message)
                (when (re-seq #"^[#]" room)
                  ;; Room notified, not PM
                  (cond
                   (re-seq #"dance" message) (.say client room "\u0001ACTION dances: :D\\-<\u0001"))
                  )))

(add-listener "motd"
              (fn [msg]
                ;; msg nickserv to log in
                (println "Identifying..")
                (.say client
                      "NickServ"
                      (str "IDENTIFY " username " " password))
                (.whois client username
                        (fn [from to message]
                          (println "whois " from to message)))))

(add-listener "invite"
              (fn [channel by]
                (println "Invited to " channel " by " by)
                (.join client channel)))

(add-listener "notice"
              (fn [from to msg]
                (println "* " from "> " to ": " msg)))

(add-listener "topic"
              (fn [channel topic nick]
                (println channel " | " "Topic: " topic)))

(defn -main [& args]
  (println "Starting up lispjs"))

(set! *main-cli-fn* -main)
