;;; src/jansenh/clj-go/core.clj  ---  Go game

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

(ns jansenh.clj-go.core
  (:require [jansenh.clj-go.board :refer [create-frame]])
  (:gen-class))

;; -----------------------------------------------------------------------------
;; clj-go core namespace
;; ---------------------
;; Clojure Go Game.
;; authors:   Henning Jansen, henning.jansen@jansenh.no
;; since:     0.1.0-SNAPSHOT  2025-01-29
;; version:   0.1.2           2026-05-13
;; -----------------------------------------------------------------------------

(defn clj-go
  "Game engine starting point."
  [& args]
  (create-frame))

(defn -main
  "Application main entry point."
  [& args]
  (clj-go))
