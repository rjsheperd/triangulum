(ns triangulum.utils-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.edn       :as edn]
            [clojure.data.json :as json]
            [triangulum.utils :refer [data-response
                                      clj->transit
                                      end-with
                                      format-str
                                      kebab->snake
                                      filterm
                                      parse-as-sh-cmd
                                      mapm
                                      transit->clj]]))

(deftest end-with-test
  (testing "Appends end-value when string doesn't end with end-value."
    (is (= (end-with "path" "/")
           "path/"))

  (testing "Does not append end-value when string does end with end-value."
    (is (= (end-with "path/" "/")
           "path/")))))

(deftest format-str-test
  (testing "Allows any char after % to be used in formatting"
    (is (= (format-str "SELECT * FROM %1 WHERE %2 = %3" "table" "column" "'value'")
           "SELECT * FROM table WHERE column = 'value'"))))

(deftest kebab->snake-test
  (testing "Converts kebab-str to snake_str"
    (is (= (kebab->snake "my-string") "my_string"))
    (is (= (kebab->snake "MY-STRING") "MY_STRING"))))

(deftest parse-as-sh-cmd-test
  (testing "Parses a string into a sh friendly array"
    (is (= (parse-as-sh-cmd "psql -p 5432 -U=username -W")
           ["psql" "-p" "5432" "-U=username" "-W"]))))

(def ^:private body {:message "success"})

(deftest clj->transit->clj-test
  (testing "Convert CLJ to transit (json-encoded) and back again"
    (is (= (transit->clj (clj->transit body)) body))))

(deftest data-response-test
  (testing "Defaults to status 200, body encoded as edn"
    (let [res (data-response body)]
      (is (= (:status res) 200))
      (is (= (-> res (:headers) (get "Content-Type")) "application/edn"))
      (is (= (-> res (:body) (edn/read-string)) body))))

  (testing "Body can be encoded as JSON"
    (let [res (data-response body {:type :json})]
      (is (= (:status res) 200))
      (is (= (-> res (:headers) (get "Content-Type")) "application/json"))
      (is (= (-> res (:body) (json/read-str :key-fn keyword)) body))))

  (testing "Body can be encoded as CLJ Transit"
    (let [res (data-response body {:type :transit})]
      (is (= (:status res) 200))
      (is (= (-> res (:headers) (get "Content-Type")) "application/transit+json"))
      (prn-str (-> res (:body) (transit->clj)))
      (is (= (-> res (:body) (transit->clj)) body)))))

(def ^:private test-map {:a 1 :b 2 :c 3 :d 4})

(deftest mapm-test
  (testing "Applies f to each entry of the map, returns map."
    (is (= (mapm (fn [[k v]] [k (+ v 1)]) test-map)
           {:a 2 :b 3 :c 4 :d 5}))))

(deftest filterm-test
  (testing "Filter map using pred, returns a map"
    (is (= (filterm (fn [[_ v]] (odd? v)) test-map)
           {:a 1 :c 3}))))

