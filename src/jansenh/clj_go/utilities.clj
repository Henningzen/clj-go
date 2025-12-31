;; -----------------------------------------------------------------------------
;; File: src/jansenh/clj_go/utilities.clj
;; Author: Henning Jansen - henning.jansen@jansenh.no
;; Copyright: (c) 2025
;; License: Distributed under the GNU General Public License v3.0
;; as described in the root of this project.
;; -----------------------------------------------------------------------------
(ns jansenh.clj-go.utilities)

;; ----------------------------------------------------------------------------
;; Test data utilities
;; -------------------
;;
;;
;; authors:   Henning Jansen            henning.jansen@jansenh.no
;; since:     0.1.1-SNAPSHOT            2025-04-17
;; version:   0.1.2-SNAPSHOT            2025-12-31
;; -----------------------------------------------------------------------------

;; 
;; testing vectors and positions. .
(def numeric-board
  "Data structure with sequence numbers.

   The data-structure is a representation of a board with n by n vector of
   vectors, numbers increasing from one.
  "
  (vec (for [row (range 19)]
         (vec (for [col (range 19)]
                (inc (+ col (* row 19))))))))


(def patterned-board
  "Scattered board with cycled values.

   Evenly distributed white, black stones with empty (nil) intersects.
   The data-structure is a representation of a board with
   19 by 19 vector with values :black :white and :nil.
  "
  (->> (take 361 (cycle [:black :nil :white :nil]))
       (partition 19)
       (mapv vec)))
