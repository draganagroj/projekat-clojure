(ns view.new-recipe
  (:require [hiccup.page :as h]
            [hiccup.form :as form]
             [noir.session :as session]
             [model.model :as model]
             [view.layout :as layout]
             [compojure.core :refer [defroutes GET POST ]]
             [noir.validation :refer [has-value?]]
             [noir.response :refer [redirect]]
             
   )
  )

(defn new-recipe [message]
  "new recipe page"
(layout/common "New recipe"
(layout/navbar-new)                         
(form/form-to  {:role "form" :id "login-form" :class "form-horizontal"}[:post "/new"]
[:div {:class "form-group"}
(form/label {:class "reg-label control-label col-md-2"} "title" "Title:")
[:div.col-md-9
(form/text-field  {:class "form-control" :required "required" } :title "")]]
[:br]
[:div {:class "form-group"}
 (form/label {:class "reg-label control-label col-md-2"} "text" "Text:")
 [:div.col-md-9
(form/text-area  { :id "txt-area" :class "form-control"  :required "required" } :text )]]
[:br]
(form/submit-button {:class "btn btn-info col-md-offset-6"} "save"))
(when
(not(= message ""))
[:div
  [:div {:class "first"} message ]
     [:div.alert.alert-success.col-md-offset-2.col-md-9.message "Your recipe has been saved"] ])
  (layout/footer)
  ))

(defroutes recipe
  "new recipe routes"
 (GET "/new" [] 
        (if (nil?(session/get :user))
           (redirect "/")
           (new-recipe "")))
  (POST "/new" [title text] 
        (do(model/insert-recipe title text (session/get :user))
           (new-recipe (model/insert-def-rat ))
          )
        )
         )
  
