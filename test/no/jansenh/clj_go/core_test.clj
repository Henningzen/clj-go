(ns no.jansenh.clj-go.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [no.jansenh.clj-go.core :as sut]))

(def numeric-board [[1   2   3   4   5   6   7   8   9   10  11  12  13]
                    [14  15  16  17  18  19  20  21  22  23  24  25  26]
                    [27  28  29  30  31  32  33  34  35  36  37  38  39]
                    [40  41  42  43  44  45  46  47  48  49  50  51  52]
                    [53  54  55  56  57  58  59  60  61  62  63  64  65]
                    [66  67  68  69  70  71  72  73  74  75  76  77  78]
                    [79  80  81  82  83  84  85  86  87  88  89  90  91]
                    [92  93  94  95  96  97  98  99  100 101 102 103 104]
                    [105 106 107 108 109 110 111 112 113 114 115 116 117]
                    [118 119 120 121 122 123 124 125 126 127 128 129 130]
                    [131 132 133 134 135 136 137 138 139 140 141 142 143]
                    [144 145 146 147 148 149 150 151 152 153 154 155 156]
                    [157 158 159 160 161 162 163 164 165 166 167 168 169]])


(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board."
    (is (= 29 (sut/position 2 2 numeric-board)))
    (is (= 30 (sut/position 3 2 numeric-board)))
    (is (= 37 (sut/position 10 2 numeric-board)))
    (is (= 141 (sut/position 10 10 numeric-board)))
    (is (= 133 (sut/position 2 10 numeric-board)))))

(deftest test-pos
  (testing "We expect to find the corresponding [x y] value on the numeric board edges ."
    (is (= 1 (sut/position 0 0 numeric-board)))
    (is (= 13 (sut/position 12 0 numeric-board)))
    (is (= 169 (sut/position 12 12 numeric-board)))
    (is (= 157 (sut/position 0 12 numeric-board)))))

(deftest test-neighbours-position
  (testing "We expect to fint the neighbours positions on the numeric board."
    (is (= [[2 1] [3 2] [2 3] [1 2]] (sut/neighbours-position 2 2 numeric-board)))
    (is (= [[10 1] [11 2] [10 3] [9 2]] (sut/neighbours-position 10 2 numeric-board)))
    (is (= [[10 9] [11 10] [10 11] [9 10]] (sut/neighbours-position 10 10 numeric-board)))
    (is (= [[2 9] [3 10] [2 11] [1 10]] (sut/neighbours-position 2 10 numeric-board)))))

(def test-neighbours-value
  (testing "We expect to find the neighbours values on the numeric board."
    (is (= [nil 30 42 nil] (sut/neighbours-value 0 0 numeric-board)))
    (is (= [nil nil 26 12] (sut/neighbours-value 12 0 numeric-board)))
    (is (= [156 nil nil 168] (sut/neighbours-value 12 12 numeric-board)))
    (is (= [144 158 nil nil] (sut/neighbours-value 0 12 numeric-board)))))

