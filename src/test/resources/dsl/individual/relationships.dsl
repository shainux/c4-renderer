relationships{
    this -> some_id{
        path [(1,1) (2,10,1) (4,5)]
        label "first view label"
        labelPosition (2,3)
        description "first view description"
    }
    user_id -> this "consumes"
    this -> another_id "interacts"
    this -> one_more_id {
        path [(1,1,1) (10,50,3) (120,240,1)]
        label some label
        labelPosition (20,20,2)
        description "some description"
    }
}
