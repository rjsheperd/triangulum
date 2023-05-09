(ns triangulum.shell
  (:require [clojure.java.io    :as io]
            [clojure.java.shell :as sh]
            [triangulum.logging :refer [log-str]]
            [triangulum.utils   :refer [parse-as-sh-cmd]]))

(def ^:private path-env (System/getenv "PATH"))

(defn sh-wrapper
  "Executes `commands` inside of directory `dir` with a map of environment variables `env`."
  [dir env & commands]
  (io/make-parents (str dir "/dummy"))
  (sh/with-sh-dir dir
    (sh/with-sh-env (merge {:PATH path-env} env)
      (doseq [cmd commands]
        (log-str cmd)
        (let [{:keys [out err]} (apply sh/sh (parse-as-sh-cmd cmd))]
          (log-str "out: "   out)
          (log-str "error: " err))))))

(clojure.string/join )
