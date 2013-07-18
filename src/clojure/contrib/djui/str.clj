(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "String helper functions."}
  clojure.contrib.djui.str
  (:require [clojure.edn :as edn]))


(defn safe-parse
  "Parse a clojure term without evaluating anything.
  Deprecated: Use clojure.edn/read-string instead."
  {:added "1.4"
   :deprecated "2.0"}
  [str]
  (edn/read-string str))
