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


(comment ;; Go string

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
  (update-go-string first-string {:player :black :stones #{[1 2]} :liberties #{[2 2] [2 3]}})

  ;; Players don't match - OK (returns original)
  (update-go-string first-string {:player :white :stones #{[2 2]} :liberties #{[2 2] [2 3]}})

  ;; Initial player invalid - Throws ex-info
  (try
    (update-go-string {:player :red} {:player :black :stones #{[1 1]}})
    (catch clojure.lang.ExceptionInfo e
      (println "Caught Exception:")
      (println "  Message:" (.getMessage e))
      (println "  Data:" (ex-data e))))

  ;; Incoming player invalid - Throws ex-info
  (try
    (update-go-string second-string {:player :non-valid
                                     :stones #{[0 0] [0 1]}
                                     :liberties 2})
    (catch clojure.lang.ExceptionInfo e
      (println "\nCaught Exception:")
      (println "  Message:" (.getMessage e))
      (println "  Data:" (ex-data e))))

  ;; Nothing to merge - OK (returns original)
  (update-go-string first-string)

  ;; --->
)

(comment ;; Testing remove-liberity

  (def initial-board (go-string {:player :black
                                 :stones #{[1 1] [1 2]}
                                 :liberties #{[2 2] [2 3]}}))

  (-> initial-board
      :liberties
      count
      (= 2))

  (-> (remove-liberty initial-board [2 3])
      :liberties
      count
      (= 1))

  (-> initial-board
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
     (-> empty-board
         (update-board 0 0   :black)
         (update-board 12 0  :black)
         (update-board 1 1   :black)
         (update-board 1 1   :black)
         (update-board 1 2   :black)
         (update-board 1 3   :black)
         (update-board 11 2  :black)
         (update-board 11 1  :white)
         (update-board 1 9   :white)
         (update-board 1 10  :white)
         (update-board 1 11  :white)
         (update-board 0 12  :black)
         (update-board 12 12 :black)
         (tb/symbolic-board))))

  ;;--->comment
  )
