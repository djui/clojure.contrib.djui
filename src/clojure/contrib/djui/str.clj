(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "String helper functions."}
  clojure.contrib.djui.str
  (:import java.util.UUID)
  (:require [clojure.edn :as edn]))


(defn uuid
  "Create a UUID string."
  {:added "2.0"}
  []
  (.toString (UUID/randomUUID)))

(defn safe-parse
  "Parse a clojure term without evaluating anything.
  Deprecated: Use clojure.edn/read-string instead."
  {:added "1.4"
   :deprecated "2.0"}
  [str]
  (edn/read-string str))
