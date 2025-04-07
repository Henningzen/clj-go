(ns dev)

(defn go-string
  "The iniital shape of a go-string."
  [m]
  (let [player    (:player m)
        stones    (set (:stones m))
        liberties (:liberties m)]
    [player stones liberties]))

(comment

  (def initial-string (go-string {:player :white :stones [[0 0] [0 1] [0 1] [0,0]] :liberties nil}))
  (go-string initial-string)

  ;;---> comment
  )
