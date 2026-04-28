package com.example.news.api.dto.jpa;

public class EntityDTO {
    private int id;
    private String value;
    private EntityTypeDTO entityTpe;

    

    public EntityDTO() {
    }


    public EntityDTO(int id, String value, EntityTypeDTO entityTpe) {
        this.id = id;
        this.value = value;
        this.entityTpe = entityTpe;
    }
 
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public EntityTypeDTO getEntityTpe() {
        return this.entityTpe;
    }

    public void setEntityTpe(EntityTypeDTO entityTpe) {
        this.entityTpe = entityTpe;
    }


}
