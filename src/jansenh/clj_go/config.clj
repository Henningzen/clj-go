;;; config.clj --- clj-go.config

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

(ns jansenh.clj-go.config
  {:author "Henning Jansen"
   :doc    "clj-go immutable configurations."
   :added "0.1.3"})

;;; Grid size, represeting the board as 'grid-size * grid-size
(def grid-size 9)
