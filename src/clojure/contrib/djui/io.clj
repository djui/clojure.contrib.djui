(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "I/O helper functions."}
  clojure.contrib.djui.io
  (:import [java.io File PushbackReader])
  (:require [clojure.edn :as edn]
            [clojure.java.io :refer [reader]]))


;; File

(defn read-file
  "Read content of filename and parse it using edn. Empty files return nil."
  {:added "1.10"
   :io? true}
  [filename]
  (with-open [r (PushbackReader. (reader filename))]
    (try (edn/read r)
         (catch java.lang.RuntimeException e
           (when (not= (.getMessage e) "EOF while reading")
             (throw e))))))

(defn tempfile
  "Create a tempfile and return the filepath. The file will be deleted when then
  VM terminates normally."
  {:added "1.10"
   :io? true}
  ([] (tempfile "temp" ""))
  ([name ext]
     (let [file (File/createTempFile name ext)]
       (.deleteOnExit file)
       (.getPath file))))
