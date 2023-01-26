softwareSystem_id = softwaresystem someSoftwareSystem "Description of Software System single line" {
    tags [tag1 tag2 tag3]
    url https://www.example.com/some-uri?with=params
    relationships{
        user_id -> this "consumes"
        this -> another_id "interacts"
        this -> one_more_id {
            path [(1,1,1) (10,50,3) (120,240,1)]
            label some label
            labelPosition (20,20,2)
            description "some description"
        }
    }
    docs [
        /some/relative/path.md
        /some/another/relative/path.md
    ]
    first_container_id = container "First Container" "some description"{
        description "multiline
                description must
                replace the one from the
                top"
        tags [tag11, tag12]
        properties { key1 value1 key2 "complex value 2" key3 val3}
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
        component_11_id = component component_11{

        }
        component{

        }
        component_12_id = component{
        }
        component component_13{}
    }
    second_container_id = container "Second Container" SecondContainerDescription{
            description "multiline second container's
                    description must
                    replace the one from the
                    top"
            tags [tag21, tag22]
            properties { key1 value1 key2 "complex value 2" key3 val3}
            perspectives{
                first_view_id{
                    label first view label
                    description "first view description"
                     position (1, 1, 5)
                     dimensions (100, 200)
                     relationships{
                        this -> some_id {
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
            component_21_id = component component_21{

            }
            component{

            }
            component_22_id = component{
            }
            component component_23{}
        }
}
