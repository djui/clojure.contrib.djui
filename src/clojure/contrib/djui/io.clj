(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "I/O helper functions."}
  clojure.contrib.djui.io
  (:import [java.io File PushbackReader])
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))


;; File

(defn read-file
  "Read content of file-path and parse it using edn. Empty files return nil."
  {:added "1.10"
   :io? true}
  [file-path]
  (with-open [reader (PushbackReader. (io/reader file-path))]
    (edn/read {:eof nil} reader)))

(defn read-resource-file
  "Read content of file-path as resource and parse it using edn. Empty and
  non-existing resources return nil."
  {:added "1.11"
   :io? true}
  [file-path]
  (read-file (io/resource file-path)))

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
