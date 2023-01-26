perspectives{
    first_view_id{
        label first view label
        description "first view description"
         position (1, 1, 5)
         dimensions (100, 200)
         relationships{
            this -> some_id{
                path [(1,1) (2,10,1) (4,5)]
                label "first view label"
                labelPosition (2,3)
                description "first view description"
            }
         }
    }
    second_view_id{
        label second view label
        description "second view description"
        position (1, 2, 5)
        dimensions (200, 200)
    }
}
