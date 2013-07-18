(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "String helper functions."}
  (:require [clojure.edn] :as edn)
  clojure.contrib.djui.str)


(defn safe-parse
  "Parse a clojure term without evaluating anything."
  {:added "1.4"}
  [str]
  (clojure.edn/read-string str))
