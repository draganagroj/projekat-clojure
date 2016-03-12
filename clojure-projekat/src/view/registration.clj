(ns view.registration
   (:require [hiccup.page :as h]
            [hiccup.form :as form]
             [noir.session :as session]
             [model.model :as model]
             [view.layout :as layout]
             [compojure.core :refer [defroutes GET POST routes]]
             [noir.response :refer [redirect]]
              [noir.validation :refer [has-value?]]
             )
  )

(defn registration-page [message]
  " registration page"
(layout/common "Register"
               [:div.row
[:div.col-md-offset-3.col-md-6 
 [:div {:class "border-div"}
 [:h2.col-md-offset-4 {:id "reg-title"} "Registration form"]
(form/form-to  {:role "form" :id "reg-form" :class "form-horizontal"} [:post "/register"]             
[:div {:class "form-group"}
(form/label {:class "reg-label control-label col-md-2"} "name" "username:")
[:div.col-md-9
(form/text-field  {:class "form-control"}"name")]]
[:br]
 [:div {:class "form-group"}
(form/label {:class "reg-label control-label col-md-2"} "pass" "password:")
[:div.col-md-9
(form/password-field {:class "form-control"  :required "required"} "pass")]]
[:br]
 [:div {:class "form-group"}
(form/label {:class "reg-label control-label col-md-2"  :required "required"}"pass1" "retype password:")
[:div.col-md-9
(form/password-field  {:class "form-control"  :required "required"}"pass1")]]
[:br]
(form/submit-button {:class "btn btn-info col-md-offset-5"} "create account"))
]
(when
(and  (has-value? message) (not(= message "Error"))) 
[:div
  [:div {:class "first"} message ]
[:div.alert.alert-success "You successfuly registered"] ]

)  
(when (and (has-value? message ) (= message "Error") )
  [:div
  [:div {:class "first"} message ]
 [:div.alert.alert-danger message]])
]]
               
[:div.row.col-md-offset-7
[:p.col-md-offset-8 {:style "color:#555"} "return to homepage"]
 [:a.col-md-offset-9 {:href "/"} 
 [:span.glyphicon.glyphicon-arrow-right {:style "font-size:3em ; color:#46b8da"}]]
 ]
 

  )
)


(defn registration-handler [name pass pass1]
  "registration validation"
   (if (= pass pass1)  
              (do 
             
              (model/create-user name pass)
              
            (registration-page  (model/rating-def name)  
              )
              )  
            (registration-page "Error")
  )
   )

(defroutes registration
  "registration routes"
  (GET "/register" [] (registration-page ""))
 (POST "/register" [name pass pass1]
       (registration-handler name pass pass1)
         ))
          
    