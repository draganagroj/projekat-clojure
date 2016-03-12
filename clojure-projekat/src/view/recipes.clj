(ns view.recipes
 (:require [hiccup.page :as h]
            [hiccup.form :as form]
             [ring.util.anti-forgery :as anti-forgery]
             [noir.session :as session] 
             [model.model :as model]
             [view.layout :as layout]
             [compojure.core :refer [defroutes GET POST ]]
             [noir.response :refer [redirect]]
             
  )
  )
(defn recipe [recipes]
  "my recipes page"
(layout/common "Recipes"
(layout/navbar-my-recipes)
 [:div.containter.recipes-containter
  [:table.col-md-8.table-st
   [:tbody
     (for [recipe recipes]
        [:tr.td-st
     [:td.td-1 (:title recipe)]
     [:td.td-2 (str (subs (:body recipe) 0 200) "...") [:a {:href (str "/show/" (:id recipe))} "  see whole recipe"]]
     ]
      )  
   ]
   
]  ]          
(layout/footer)                  )
  )

(defroutes my-recipes
  "my recipe routes"
  (GET "/myrecipes" []
       (if (nil?(session/get :user))
         (redirect "/")
         (recipe (model/my-recipes (session/get :user)) ))
       )
  )