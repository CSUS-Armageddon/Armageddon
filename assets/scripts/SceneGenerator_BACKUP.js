/**
 * %%% AUTO-GENERATED %%%
 * Scenery Configuration Script
 * Generated: 2019-04-25T11:30:15.994-07:00[America/Los_Angeles]
 * %%% AUTO-GENERATED %%%
 */

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneManager,
	Packages.ray.rage.rendersystem.Renderable.Primitive
);

with (JavaPackages) {
    function generateSceneObjects(sm) {
        /******** >>> BEGIN Object_1 <<< ********/
        var entity_1 = sm.createEntity('Object_1_Entity', 'Tree_01.obj');
        entity_1.setPrimitive(Primitive.TRIANGLES);
        var node_1 = sm.getRootSceneNode().createChildSceneNode('Object_1_Node');
        node_1.setLocalPosition(-39.425003, 9.252451, 10.125);
        node_1.setLocalRotation(Matrix3f = [ -0.00000 |   0.00000 |   1.00000]
           [  0.00000 |   1.00000 |   0.00000]
           [ -1.00000 |   0.00000 |  -0.00000]);
        node_1.setLocalScale(1.0, 1.0, 1.0);
        node_1.attachObject(entity_1);
        /******** >>> END Object_1 <<< ********/

        /******** >>> BEGIN Object_2 <<< ********/
        var entity_2 = sm.createEntity('Object_2_Entity', 'Tree_01.obj');
        entity_2.setPrimitive(Primitive.TRIANGLES);
        var node_2 = sm.getRootSceneNode().createChildSceneNode('Object_2_Node');
        node_2.setLocalPosition(-55.625015, 9.252451, -40.500004);
        node_2.setLocalRotation(Matrix3f = [ -0.00000 |   0.00000 |   1.00000]
           [  0.00000 |   1.00000 |   0.00000]
           [ -1.00000 |   0.00000 |  -0.00000]);
        node_2.setLocalScale(1.0, 1.0, 1.0);
        node_2.attachObject(entity_2);
        /******** >>> END Object_2 <<< ********/

        /******** >>> BEGIN Object_3 <<< ********/
        var entity_3 = sm.createEntity('Object_3_Entity', 'Tree_01.obj');
        entity_3.setPrimitive(Primitive.TRIANGLES);
        var node_3 = sm.getRootSceneNode().createChildSceneNode('Object_3_Node');
        node_3.setLocalPosition(31.449993, 14.889835, -60.750015);
        node_3.setLocalRotation(Matrix3f = [ -0.00000 |   0.00000 |   1.00000]
           [  0.00000 |   1.00000 |   0.00000]
           [ -1.00000 |   0.00000 |  -0.00000]);
        node_3.setLocalScale(1.0, 1.0, 1.0);
        node_3.attachObject(entity_3);
        /******** >>> END Object_3 <<< ********/

        /******** >>> BEGIN Object_4 <<< ********/
        var entity_4 = sm.createEntity('Object_4_Entity', 'Tree_02.obj');
        entity_4.setPrimitive(Primitive.TRIANGLES);
        var node_4 = sm.getRootSceneNode().createChildSceneNode('Object_4_Node');
        node_4.setLocalPosition(-9.050003, 9.258475, -16.199997);
        node_4.setLocalRotation(Matrix3f = [ -0.00000 |   0.00000 |   1.00000]
           [  0.00000 |   1.00000 |   0.00000]
           [ -1.00000 |   0.00000 |  -0.00000]);
        node_4.setLocalScale(1.0, 1.0, 1.0);
        node_4.attachObject(entity_4);
        /******** >>> END Object_4 <<< ********/

        /******** >>> BEGIN Object_5 <<< ********/
        var entity_5 = sm.createEntity('Object_5_Entity', 'Tree_02.obj');
        entity_5.setPrimitive(Primitive.TRIANGLES);
        var node_5 = sm.getRootSceneNode().createChildSceneNode('Object_5_Node');
        node_5.setLocalPosition(-33.35, 9.252451, -32.399994);
        node_5.setLocalRotation(Matrix3f = [ -0.00000 |   0.00000 |   1.00000]
           [  0.00000 |   1.00000 |   0.00000]
           [ -1.00000 |   0.00000 |  -0.00000]);
        node_5.setLocalScale(1.0, 1.0, 1.0);
        node_5.attachObject(entity_5);
        /******** >>> END Object_5 <<< ********/

        /******** >>> BEGIN Object_6 <<< ********/
        var entity_6 = sm.createEntity('Object_6_Entity', 'Tree_02.obj');
        entity_6.setPrimitive(Primitive.TRIANGLES);
        var node_6 = sm.getRootSceneNode().createChildSceneNode('Object_6_Node');
        node_6.setLocalPosition(-74.23486, 0.061274514, 32.804543);
        node_6.setLocalRotation(Matrix3f = [ -0.95106 |   0.00000 |   0.30902]
           [  0.00000 |   1.00000 |   0.00000]
           [ -0.30902 |   0.00000 |  -0.95106]);
        node_6.setLocalScale(1.0, 1.0, 1.0);
        node_6.attachObject(entity_6);
        /******** >>> END Object_6 <<< ********/

    }

}
