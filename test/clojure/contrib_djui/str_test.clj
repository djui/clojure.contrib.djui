(ns clojure.contrib-djui.str-test
  (:use clojure.test)
  (:use clojure.contrib-djui.str))


(deftest safe-parse-test
  (is (= '(prn {:a 1.0 :b "foo" :c [1 2 3]})
         (safe-parse "(prn {:a 1.0 :b \"foo\" :c [1 2 3]})"))))
