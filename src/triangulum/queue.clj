(ns triangulum.queue
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.exchange  :as le]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))

(def ^{:const true} default-exchange-name "")
(defonce ^:private conn (atom nil))
(defonce ^:private ch (atom nil))

(defn connect!
  ([]
   (reset! conn (rmq/connect))
   (reset! ch (lch/open conn)))
  ([config]
   (reset! conn (rmq/connect config))
   (reset! ch (lch/open conn))))

(defn open-queue!
  [q-name & [q-config]]
  (lq/declare @ch q-name (merge {:exclusive false :auto-delete true} q-config)))

(defn create-consumer!
  [qname topic handler]
  (let [q (lc/subscribe @ch qname handler {:auto-ack true})]
    (lq/bind @ch (:queue q) default-exchange-name {:routing-key topic})))

(defn send!
  [qname message topic content-type]
  (lb/publish @ch default-exchange-name qname message {:content-type content-type :type topic}))

