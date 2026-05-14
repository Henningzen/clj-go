;;; src/jansenh/clj-go/utilities.clj  ---  Go game utilities

;   Copyright (c) Henning Jansen 2025 - 2026
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 2.0 (https://opensource.org/license/epl-2-0)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license. You must not remove this notice, or any other,
;   from this software.
;
;; Author:  Henning Jansen - henning.jansen@jansenh.no
;; Date:    September 2025
;; License: Eclipse Public License 2.0 - http://www.eclipse.org/legal/epl-2.0
;;-----------------------------------------------------------------------------

(ns jansenh.clj-go.utilities)

;; ----------------------------------------------------------------------------
;; Test data utilities
;; -------------------
;;
;;
;; authors:   Henning Jansen, henning.jansen@jansenh.no
;; since:     0.1.1-SNAPSHOT  2025-04-17
;; version:   0.1.2-SNAPSHOT  2026-05-13
;; -----------------------------------------------------------------------------


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
