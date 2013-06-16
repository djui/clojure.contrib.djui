(ns clojure.contrib-djui.coll-test
  (:use clojure.test)
  (:use clojure.contrib-djui.coll))


(deftest gapmap-test
  (are [x y] (= x y)
       [1 2 1] (gapmap + [0 1 1 0])
       [2]     (gapmap + [1 1])
       [1]     (gapmap + [1])
       []      (gapmap + [])
       nil     (gapmap + nil)))
