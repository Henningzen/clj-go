;; -----------------------------------------------------------------------------
;; File: src/no/jansenh/clj_go/utilities.clj
;; Author: Henning Jansen - henning.jansen@jansenh.no
;; Copyright: (c) 2025
;; License: Distributed under the GNU General Public License v3.0
;; as described in the root of this project.
;; -----------------------------------------------------------------------------
(ns no.jansenh.clj-go.utilities)

;; ----------------------------------------------------------------------------
;; Test data utilities
;;

;; We got a data-structure with numbers only, used for 
;; testing vectors and positions. The data-structure is
;; a representation of a board with 13 by 13 vector,
;; numbers 1 to 169.
(def numeric-board
  (vec (for [row (range 19)]
         (vec (for [col (range 19)]
                (inc (+ col (* row 19))))))))

;; We got a scattered board with cycled values, evenly
;; distributed white, black stones with empty intersects.
;; The data-structure is a representation of a board with
;; 13 by 13 vector with values :black :white and :nil.
(def patterned-board
  (->> (take 361 (cycle [:black :nil :white :nil]))
       (partition 19)
       (mapv vec)))
