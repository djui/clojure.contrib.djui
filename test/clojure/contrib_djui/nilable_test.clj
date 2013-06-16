(ns clojure.contrib-djui.nilable-test
  (:use clojure.test)
  (:use clojure.contrib-djui.nilable))


(deftest coalesce-test
  (is (= false (coalesce nil false 1)))
  (is (= 1 (coalesce nil 1)))
  (is (= 1 (coalesce 1 2)))
  (is (= nil (coalesce (first []))))
  (is (= nil (coalesce nil nil)))
  (is (= nil (coalesce))))

(deftest ?>=-test
  (is (= true (?>= 1)))
  (is (= true (?>= 2 1)))
  (is (= true (?>= 3 2 1)))
  (is (= true (?>= nil)))
  (is (= false (?>= nil 1)))
  (is (= false (?>= nil 1 1)))
  (is (= true (?>= nil nil)))
  (is (= true (?>= nil nil nil)))
  (is (= false (?>= 1 2)))
  (is (= false (?>= 1 2 3)))
  (is (thrown? clojure.lang.ArityException (?>=)))
  (is (thrown? NullPointerException (?>= 1 nil))))
