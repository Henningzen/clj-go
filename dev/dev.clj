;;; dev.clj --- clj-go.dev namespace for exploration and development, work-in-progres

;   Copyright (c) Henning Jansen 2025 - 2026
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 2.0 (https://opensource.org/license/epl-2-0)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license. You must not remove this notice, or any other,
;   from this software.

;; Author:  Henning Jansen - henning.jansen@jansenh.no
;; Date:    September 2025
;; License: Eclipse Public License 2.0 - http://www.eclipse.org/legal/epl-2.0

(ns dev
  ^{:author "Henning Jansen"
    :doc    "clj-go.dev namespace for exploration and development, work-in-progres"
    :added "0.1.2"}
  (:require [user :refer [println-point->terminal-board println-point->terminal-board]]
            [jansenh.clj-go.engine :refer :all]
            [jansenh.clj-go.terminal-board :refer [symbolic-board]]
            [jansenh.clj-go.utilities :refer [stone  empty-board-vector]]))



;; TODO; These are valuable tests!
(->> (empty-board-vector 19)
     (update-position-at-board {:x-pos 1 :y-pos 1 :player :black})
     (value-at-position {:x-pos 1 :y-pos 1 :player :black}))

(update-position-at-board {:x-pos 1 :y-pos 1 :player :black} (empty-board-vector 19))

(->> empty-board-vector (update-position-at-board {:x-pos 1 :y-pos 1 :value :black}))

(comment
  (println-point->terminal-board {:player :black :x-pos 18 :y-pos 18} (empty-board-vector 19))
  ;; --->
  )

(comment
  ;; ---------------------------------------------------------------------------
  ;; Go string
  ;;

  ;; We now got an arity for calling an empty go-string,
  (def empty-go-string (go-string))
  (assoc empty-go-string :player :black)

  (-> (go-string)
      (assoc :player :black)
      (assoc :stones #{[0 0]})
      (assoc :liberties #{[1 0] [0 1]}))

  ;; Input with :stones as a set of vectors
  (go-string {:player :black :stones #{[1 1] [1 2]} :liberties #{[2 2] [2 3]}})

  ;; Input with :stones as a vector of vectors
  (go-string {:player :white :stones [[2 2] [3 3]] :liberties #{[3 3] [2 3]}})

  ;; Input with :stones missing
  (go-string {:player :black :liberties #{[1 1] [1 2]}}) ;; TODO:

  ;; Input with :liberties missing
  (go-string {:player :white :stones #{[0 0]}})

  )

(comment
  ;; ---------------------------------------------------------------------------
  ;; Testing updates to  Go strings
  ;;

  (def first-string {:player :black :stones #{[1 1]} :liberties #{[2 2] [2 3]}})
  (def second-string {:player :white :stones #{[5 5]} :liberties #{[2 2] [2 3]}})

  ;; Valid merge - OK
  (go-string first-string {:player :black :stones #{[1 2]} :liberties #{[2 2] [2 3]}})

  ;; Players don't match - OK (returns original)
  (go-string first-string {:player :white :stones #{[2 2]} :liberties #{[2 2] [2 3]}})

  ;; Initial player invalid - Throws ex-info
  (try
    (go-string {:player :red} {:player :black :stones #{[1 1]}})
    (catch clojure.lang.ExceptionInfo e
      (println "Caught Exception:")
      (println "  Message:" (.getMessage e))
      (println "  Data:" (ex-data e))))

  ;; Incoming player invalid - Throws ex-info
  (try
    (go-string second-string {:player :non-valid
                                     :stones #{[0 0] [0 1]}
                                     :liberties 2})
    (catch clojure.lang.ExceptionInfo e
      (println "\nCaught Exception:")
      (println "  Message:" (.getMessage e))
      (println "  Data:" (ex-data e))))

  ;; Nothing to merge - OK (returns original)
  (go-string first-string)

  ;; --->
)


(comment
  ;; ---------------------------------------------------------------------------
  ;; Let's apply a go-string or two on a board.
  ;;

  (def initial-white-string (go-string {:player :white :stones #{[0 1] [0 2]} :liberties #{[2 2] [2 3]}}))
  (def initial-black-string (go-string {:player :black :stones #{[1 1] [1 2]} :liberties #{[2 2] [2 3]}}))

  (go-string->board initial-black-string empty-board)
  (->> empty-board
       (go-string->board initial-black-string)
       (go-string->board initial-white-string)
       (tb/symbolic-board)
       (println))

  ;;--->
)



(comment

  (def initial-white-string (go-string {:player :white :stones #{[1 1] [1 2]} :liberties #{[2 2] [2 3]}}))
  (def initial-black-string (go-string {:player :black :stones #{[1 1] [1 2]} :liberties #{[2 2] [2 3]}}))

  ;; TODO:  We want to apply go-string(s) to a board.  TODO!!!
  (-> tb/symbolic-board initial-white-string)
  (tb/symbolic-board empty-board #_initial-white-string)

  ;;--->
  )

(comment
  ;; ---------------------------------------------------------------------------
  ;; Testing remove-liberity
  ;;

  (def initial-go-string (go-string {:player :black
                                     :stones #{[1 1] [1 2]}
                                     :liberties #{[2 2] [2 3]}}))

  (-> initial-go-string
      :liberties
      count
      (= 2))

  (-> (remove-liberty initial-go-string [2 3])
      :liberties
      count
      (= 1))

  (-> initial-go-string
      (remove-liberty [2 2])
      (remove-liberty [2 3])
      :liberites
      count
      (= 0))

  ;;---> comment
  )


(comment
  ;; ---------------------------------------------------------------------------
  ;; Game board vector and map
  ;;
  ;;    - first pos is the  x, column dimension, and
  ;;    - second pos is the y, row dimension.
  ;;

  (do
    (println "- - - - - - - - - - - - -")
    (println
     (->> empty-board

          ;; Column 0 , 'A' from black
          (update-position-at-board (stone :black 0 0)) ;; Upper left corner
          (update-position-at-board (stone :black 0 1))
          (update-position-at-board (stone :black 0 2))
          (update-position-at-board (stone :black 0 3))
          (update-position-at-board (stone :black 0 4))
          (update-position-at-board (stone :black 0 5))
          (update-position-at-board (stone :black 0 6))
          (update-position-at-board (stone :black 0 7))
          (update-position-at-board (stone :black 0 8)) ;; Lower left corner

          ;; Column 8, 'J' from black
          (update-position-at-board (stone :white 8 0)) ;; Upper right corner
          (update-position-at-board (stone :black 8 1))
          (update-position-at-board (stone :black 8 2))
          (update-position-at-board (stone :black 8 3))
          (update-position-at-board (stone :black 8 4))
          (update-position-at-board (stone :black 8 5))
          (update-position-at-board (stone :black 8 6))
          (update-position-at-board (stone :black 8 7))
          (update-position-at-board (stone :black 8 8)) ;; Lower right corner

          ;; Row 0, '9' from Black
          (update-position-at-board (stone :black 0 0)) ;; Upper left corner
          (update-position-at-board (stone :white 1 0))
          (update-position-at-board (stone :white 2 0))
          (update-position-at-board (stone :white 3 0))
          (update-position-at-board (stone :white 4 0))
          (update-position-at-board (stone :white 5 0))
          (update-position-at-board (stone :white 6 0))
          (update-position-at-board (stone :white 7 0))
          (update-position-at-board (stone :white 8 0)) ;; Upper right corner

          ;; Row 8, '1' from black
          (update-position-at-board (stone :white 0 8)) ;; Lower left corner
          (update-position-at-board (stone :white 1 8))
          (update-position-at-board (stone :white 2 8))
          (update-position-at-board (stone :white 3 8))
          (update-position-at-board (stone :white 4 8))
          (update-position-at-board (stone :white 5 8))
          (update-position-at-board (stone :white 6 8))
          (update-position-at-board (stone :white 7 8))
          (update-position-at-board (stone :black 8 8)) ;; Lower right corner

          ;; Perfect middle
          (update-position-at-board (stone :black 4 4)) ;; perfect middle (9x9)
          (update-position-at-board (stone :white 4 3))
          (update-position-at-board (stone :white 3 4))
          (update-position-at-board (stone :white 5 4))
          (update-position-at-board (stone :white 4 5))
          (tb/symbolic-board))))

  ;;---> comment
  )



(comment
  ;; ---------------------------------------------------------------------------
  ;; We can capture live board states from the Graphical Board
  ;; and use in REPL, e.g. for furher exploration with terminal-board
  ;; or create unit-tests on the data

  (def example-saved-board-state
    [[:black nil nil nil :black nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil nil nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil :black nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil nil nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil :white nil nil nil :white]])

  (println
   (->> example-saved-board-state
        (tb/symbolic-board)))

  ;; ---> comment
  )
