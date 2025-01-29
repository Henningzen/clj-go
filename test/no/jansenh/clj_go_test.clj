(ns no.jansenh.clj-go-test
  (:require [clojure.test :refer :all]
            [no.jansenh.clj-go :refer :all]))

(deftest a-test
  (testing "I will not fail."
    (is (= 1 1))))
