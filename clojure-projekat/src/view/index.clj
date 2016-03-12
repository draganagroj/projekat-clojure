(ns view.index
  (:require [hiccup.page :as h]
            [hiccup.form :as form]        
             [noir.session :as session]
              [noir.validation :as valid]
             [model.model :as model]
             [view.layout :as layout]
             [compojure.core :refer [defroutes GET POST routes]]
             
   ) )



(defn most-liked []
  "recipes sorted desc based on likes"
  [:div.containter.recipes-containter
   [:p1.col-md-offset-4.font-head "Most popular"]
      [:table.col-md-8.table-st
       [:tbody
       (for [recipe (model/best-recipe)]
         [:tr.td-st
           [:td.td-1 (:title recipe)]
           [:td.td-2 (str (subs (:body recipe) 0 200) "...") [:a {:href (str "/show/" (:id recipe))} "  see whole recipe"]]
       ]
      )         
       ]
   
    ]
        ]        
  )

(defn searched [search]
  "display search results "
  (if (empty?(model/search-recipe search))
      (list [:p1.col-md-offset-2.font-head "Sorry, there are no recipes with that name"])
      (list      
  [:div.containter.recipes-containter
   [:p1.col-md-offset-4.font-head "Search results"]
      [:table.col-md-8.table-st
       [:tbody
         (for [recipe (model/search-recipe search)]
         [:tr.td-st
          [:td.td-1 (:title recipe)]
          [:td-td-2  (str (subs (:body recipe) 0 200) "...") [:a {:href (str "/show/" (:id recipe))} "  see whole recipe"]]
       ]
      )      
       ]
   
    ]
        ]        
          )  )  
  )
  

(defn index
  "home page"
   [search]
  ( layout/common "Index"
                  (layout/navbar)
                 (if (empty? search)
                    (most-liked)
                    (searched search)
                   )
                   (layout/footer)
           )
  )

(defroutes index-route
  "home routes"
  (GET "/" [] (index ""))
  (POST "/" [search] (index search) )
  )
