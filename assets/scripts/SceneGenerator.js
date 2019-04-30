/**
 * %%% AUTO-GENERATED %%%
 * Scenery Configuration Script
 * Generated: 2019-04-29T19:23:04.944-07:00[America/Los_Angeles]
 * %%% AUTO-GENERATED %%%
 */

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneManager,
   Packages.ray.rml.Matrix3f,
	Packages.ray.rage.rendersystem.Renderable.Primitive
);

with (JavaPackages) {
    function generateSceneObjects(sm, container) {
        var conatiner = 'SCENE_OBJECTS';
        var Matrix3f = Java.type('ray.rml.Matrix3f');
        /******** >>> BEGIN Object_1 <<< ********/
        var entity_1 = sm.createEntity('Object_1_Entity', 'Tree_01.obj');
        entity_1.setPrimitive(Primitive.TRIANGLES);
        var node_1 = sm.getSceneNode(container).createChildSceneNode('Object_1_Node');
        node_1.setLocalPosition(-81.950035, 0.0, 54.675014);
        var rotArray = [-4.371139E-8, 0.0, -1.0, 0.0, 0.99999994, 0.0, 1.0, 0.0, -4.371139E-8];
        node_1.setLocalRotation(Matrix3f['createFrom(float[])'](rotArray));
        node_1.setLocalScale(4.9629555, 4.9629555, 4.9629555);
        node_1.attachObject(entity_1);
        /******** >>> END Object_1 <<< ********/

        /******** >>> BEGIN Object_3 <<< ********/
        var entity_3 = sm.createEntity('Object_3_Entity', 'Tree_02.obj');
        entity_3.setPrimitive(Primitive.TRIANGLES);
        var node_3 = sm.getSceneNode(container).createChildSceneNode('Object_3_Node');
        node_3.setLocalPosition(-118.40006, 0.0, 60.75002);
        var rotArray = [-4.371139E-8, 0.0, -1.0, 0.0, 0.99999994, 0.0, 1.0, 0.0, -4.371139E-8];
        node_3.setLocalRotation(Matrix3f['createFrom(float[])'](rotArray));
        node_3.setLocalScale(4.9629555, 4.9629555, 4.9629555);
        node_3.attachObject(entity_3);
        /******** >>> END Object_3 <<< ********/

    }

}
