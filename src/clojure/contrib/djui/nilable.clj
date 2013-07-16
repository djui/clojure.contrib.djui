(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Nilable (Nullable) helper functions."}
  clojure.contrib.djui.nilable)

(defmacro coalesce
  "Returns nil if all of its arguments are nil, otherwise it returns the first
  non-nil argument. Similar to (first (filter #(not (nil? %)) coll)) but takes
  arguments instead of a collection."
  {:added "1.0"}
  ([] nil)
  ([x] x)
  ([x & rest]
     `(let [c# ~x]
        (if (not (nil? c#))
          c#
          (coalesce ~@rest)))))

(defn ?>=
  "Nilable variant of >=, but also returns false if only first argument is nil
  or true if all arguments are nil."
  {:added "1.0"}
  ([& more]
     (if (nil? (first more))
       (apply = more) ; assert all nil
       (apply >= more)))) ; fallback default behaviour

;?>
;?<=
;?<
;?=
;?+
;?-
;?*
;?/
;?%
;
;>=?
;>?
;<=?
;<?
;=?
;+?
;-?
;*?
;/?
;%?
;
;?>=?
;?>?
;?<=?
;?<?
;?=?
;?+?
;?-?
;?*?
;?/?
;?%?
