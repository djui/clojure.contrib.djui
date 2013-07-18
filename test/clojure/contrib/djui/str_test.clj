(ns clojure.contrib.djui.str-test
  (:require [clojure.contrib.djui.io :refer [tempfile]])
  (:use clojure.contrib.djui.str
        clojure.test))


(deftest safe-parse-test
  (is (= '(prn {:a 1.0 :b "foo" :c [1 2 3]})
         (safe-parse "(prn {:a 1.0 :b \"foo\" :c [1 2 3]})"))))
