/**
 * %%% AUTO-GENERATED %%%
 * Scenery Configuration Script
 * Generated: 2019-04-29T19:48:39.711-07:00[America/Los_Angeles]
 * %%% AUTO-GENERATED %%%
 */

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneManager,
   Packages.ray.rml.Matrix3f,
	Packages.ray.rage.rendersystem.Renderable.Primitive,
   Packages.a3.editor.avatar.PlaceableAvatars
);

with (JavaPackages) {
    function generateSceneObjects(sm, container) {
        var conatiner = 'SCENE_OBJECTS';
        var Matrix3f = Java.type('ray.rml.Matrix3f');
        var PlaceableAvatars = Java.type('a3.editor.avatar.PlaceableAvatars');
        /******** >>> BEGIN Object_1 <<< ********/
        var entity_1 = sm.createEntity('Object_1_Entity', 'Tree_01.obj');
        entity_1.setPrimitive(Primitive.TRIANGLES);
        var node_1 = sm.getSceneNode(container).createChildSceneNode('Object_1_Node');
        node_1.setLocalPosition(-63.72502, 0.061274514, 50.62501);
        var rotArray = [-4.371139E-8, 0.0, -1.0, 0.0, 0.99999994, 0.0, 1.0, 0.0, -4.371139E-8];
        node_1.setLocalRotation(Matrix3f['createFrom(float[])'](rotArray));
        node_1.setLocalScale(1.0, 1.0, 1.0);
        node_1.attachObject(entity_1);
        placeableAvatarMap.put('Object_1_Entity', PlaceableAvatars.fromPlaceableAvatarFileName('Tree_01.obj'));
        /******** >>> END Object_1 <<< ********/

        /******** >>> BEGIN Object_2 <<< ********/
        var entity_2 = sm.createEntity('Object_2_Entity', 'Tree_01.obj');
        entity_2.setPrimitive(Primitive.TRIANGLES);
        var node_2 = sm.getSceneNode(container).createChildSceneNode('Object_2_Node');
        node_2.setLocalPosition(-79.92503, 0.0, 66.82502);
        var rotArray = [-4.371139E-8, 0.0, -1.0, 0.0, 0.99999994, 0.0, 1.0, 0.0, -4.371139E-8];
        node_2.setLocalRotation(Matrix3f['createFrom(float[])'](rotArray));
        node_2.setLocalScale(1.0, 1.0, 1.0);
        node_2.attachObject(entity_2);
        placeableAvatarMap.put('Object_2_Entity', PlaceableAvatars.fromPlaceableAvatarFileName('Tree_01.obj'));
        /******** >>> END Object_2 <<< ********/

        /******** >>> BEGIN Object_3 <<< ********/
        var entity_3 = sm.createEntity('Object_3_Entity', 'Tree_02.obj');
        entity_3.setPrimitive(Primitive.TRIANGLES);
        var node_3 = sm.getSceneNode(container).createChildSceneNode('Object_3_Node');
        node_3.setLocalPosition(-83.97503, 0.0, 38.475002);
        var rotArray = [-4.371139E-8, 0.0, -1.0, 0.0, 0.99999994, 0.0, 1.0, 0.0, -4.371139E-8];
        node_3.setLocalRotation(Matrix3f['createFrom(float[])'](rotArray));
        node_3.setLocalScale(1.0, 1.0, 1.0);
        node_3.attachObject(entity_3);
        placeableAvatarMap.put('Object_3_Entity', PlaceableAvatars.fromPlaceableAvatarFileName('Tree_02.obj'));
        /******** >>> END Object_3 <<< ********/

        /******** >>> BEGIN Object_4 <<< ********/
        var entity_4 = sm.createEntity('Object_4_Entity', 'Tree_02.obj');
        entity_4.setPrimitive(Primitive.TRIANGLES);
        var node_4 = sm.getSceneNode(container).createChildSceneNode('Object_4_Node');
        node_4.setLocalPosition(-94.10004, 0.0, 58.725018);
        var rotArray = [-4.371139E-8, 0.0, -1.0, 0.0, 0.99999994, 0.0, 1.0, 0.0, -4.371139E-8];
        node_4.setLocalRotation(Matrix3f['createFrom(float[])'](rotArray));
        node_4.setLocalScale(1.0, 1.0, 1.0);
        node_4.attachObject(entity_4);
        placeableAvatarMap.put('Object_4_Entity', PlaceableAvatars.fromPlaceableAvatarFileName('Tree_02.obj'));
        /******** >>> END Object_4 <<< ********/

    }

}
