;; -----------------------------------------------------------------------------
;; File: src/no/jansenh/clj_go.clj
;; Author: Henning Jansen - henning.jansen@jansenh.no
;; Copyright: (c) 2025
;; License: Distributed under the GNU General Public License v3.0
;; as described in the root of this project.
;; -----------------------------------------------------------------------------
(ns no.jansenh.clj-go
  (:require [no.jansenh.clj-go.board :refer [create-frame]])
  (:gen-class))
;; -----------------------------------------------------------------------------
;; Clj-go
;; ------
;; Clojure Go Game.
;; authors:   Henning Jansen            henning.jansen@jansenh.no
;; since:     0.1.0-SNAPSHOT            2025-01-29
;; version:   0.1.1-SNAPSHOT            2025-06-04
;; -----------------------------------------------------------------------------

(defn clj-go
  "Game engine starting point."
  [& args]
  (create-frame))

(defn -main
  "Application main entry point."
  [& args]
  (clj-go))
