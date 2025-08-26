;; -----------------------------------------------------------------------------
;; File: src/dev/dev.clj
;; Author: Henning Jansen - henning.jansen@jansenh.no
;; Copyright: (c) 202
;; License: Distributed under the GNU General Public License v3.0
;; as described in the root of this project.
;; -----------------------------------------------------------------------------

(ns dev
  (:require [no.jansenh.clj-go.core :refer :all]
            [no.jansenh.clj-go.terminal-board :as tb]))

;; -----------------------------------------------------------------------------
;; dev.clj
;; -----------
;; Development namespace. Ephemeral hosting of tests and experiments
;;
;; authors:   Henning Jansen            henning.jansen@jansenh.no
;; since:     0.1.0-SNAPSHOT            2025-01-29
;; version:   0.1.1-SNAPSHOT            2025-04-04
;; -----------------------------------------------------------------------------

;; TODO; These are valuable tests!
(->> empty-board
     (update-position-at-board {:x-pos 1 :y-pos 1 :value :black})
     (value-at-position {:x-pos 1 :y-pos 1 :value :black}))

(update-position-at-board {:x-pos 1 :y-pos 1 :value :black} empty-board)

(->> empty-board (update-position-at-board {:x-pos 1 :y-pos 1 :value :black}))

(comment ;; Go string

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

(comment ;; Testing updates to  Go strings

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
  ;; Let's apply a go-string or two on a board. 
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
  (tb/symbolic-board empty-board #_initial-white-string)q

  ;;--->
  )

(comment ;; Testing remove-liberity

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

  ;; Game-play!
  (do
    (println "- - - - - - - - - - - - -")
    (println
     (->> empty-board
          (update-position-at-board (stone 0 0   :black))
          (update-position-at-board (stone 12 0  :black))
          (update-position-at-board (stone 1 1   :black))
          (update-position-at-board (stone 1 1   :black))
          (update-position-at-board (stone 1 2   :black))
          (update-position-at-board (stone 1 3   :black))
          (update-position-at-board (stone 11 2  :black))
          (update-position-at-board (stone 11 1  :white))
          (update-position-at-board (stone 1 9   :white))
          (update-position-at-board (stone 1 10  :white))
          (update-position-at-board (stone 1 11  :white))
          (update-position-at-board (stone 0 12  :black))
          (update-position-at-board (stone 12 12 :black))
          (tb/symbolic-board))))

  ;;--->comment
  )
