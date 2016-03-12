(ns controller.controller
  (:require [compojure.core :refer [defroutes GET POST routes]]
             [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
             [compojure.handler :as handler]
             [model.model :as model]
                [ring.util.response :as ring]
                 [hiccup.form :as form]
                [noir.response :refer [redirect]]
                [noir.session :as session]
                  [ring.util.anti-forgery :as anti-forgery]
                [ring.middleware.session.memory
                 :refer [memory-store]]
                [hiccup.page :as h]
                 [compojure.route :as route]
                  [noir.validation :as valid]
                  [noir.util.middleware :as util]
                  [view.index :refer [index-route]]
                  [view.registration :refer [registration]]
                  [view.login :refer [login]]
                  [view.new-recipe :refer [recipe]]
                  [view.show-recipe :refer [show]]
                  [view.recipes :refer [my-recipes]]
                 
                  )
  )




(defroutes app-routes
  "loading resources"
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  "application handler"
(->
(handler/site (routes
                recipe
                show
                registration
                index-route
                login
                my-recipes
                app-routes
                ))
(session/wrap-noir-session 
{:store (memory-store)})
 (valid/wrap-noir-validation))

)