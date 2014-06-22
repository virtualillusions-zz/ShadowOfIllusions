/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.basic;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.base.DefaultEntityData;

public class Main {

    public static void main(String[] args) {

        //Creating the EntityData
        EntityData entityData = new DefaultEntityData();
        
        //Creates a new EntityId, the id is handled as an object to prevent botching
        EntityId entityId = entityData.createEntity();
        //A new TestComponent is added to the Entity
        entityData.setComponent(entityId, new TestComponent("Hello World"));

        //Get a new Entity Object with TestComponents
        Entity entity = entityData.getEntity(entityId, TestComponent.class);
        //Get the Component and display the value
        TestComponent testComponent = entity.get(TestComponent.class);
        System.out.println(testComponent.getValue());

        //Overwrite the existing component
        entity.set(new TestComponent("New Value"));
        System.out.println(testComponent.getValue());

        //Remove the Entity from the data
        entityData.removeEntity(entity.getId());
    }
}
