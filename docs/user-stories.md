## As a User I want to design and simulate basic electrical circuits to get an intuitive sense of what is happening at the lower level of computing.

#### 1. As a User I want to see all components, so that I can get an overview of what’s available.
  * Create a rectangle looking panel that shows the name of the component
  * Add a picture to the component
  * Create a list object that appends these panels from top to bottom.
  * Make the list object scrollable.
#### 2. As a User I want to be able to see details about each available component, so that I can gain insight into what they’re doing.
  * Add name and title to components
  * Create a GUI object that can display such information
  * Show this object when a component is being hovered (from the list)
#### 3. As a User I want to be able to search for a specific component, so that I can quickly find the component I need.
  * We need to create a text field GUI object
  * Add filter function to the list object that displays the components.
  * Call the filter function onEnter input from the search field 
#### 4. As a User I want to be able to place components on a workspace, so that I can use them.
  * We need a Workspace object that can store the components and their coordinates (so a wrapper will be needed)
  * We need a Camera that shows a viewport of the workspace. The camera does NOT need to have movement yet (I.e. panning).
  * We need to be able to drag components and show that we are dragging them, by attaching their icon to the mouse while dragging.
  * Workspace will need to listen for mouse release in case a component is being dragged. 
  * On release, place component on workspace (I.e. add to list and such)
#### 5. As a User I want to be able to get a sense of how big a component actually is and where exactly it will land before placing it, so that I can place components nicely and efficiently.
  * When dragging over the workspace, show a “real size” scaled preview of the component.
#### 6. As a User I want to be able to pan across the workspace so that I can utilize more space for my circuits.
  * Add a tool to switch between normal/pan mode
  * When in pan mode, move camera on mouse drag
#### 7. As a User I want to be able to connect components, so that I create more complex structures.
  * We create a Wire object that connects components and is created by clicking on 2 endpoints.
  * We want to draw a wire between the connections
  * We want to show a preview when the mouse is looking for an endpoint
  * Pressing ESC should cancel the wire creation and thus remove the preview
#### 8. As a User I want to be able to remove components, in case I mess up.
  * Add wire tool that deletes entire wires
  * Change so that the wire tool only deletes the selected wire until forks are reached.
#### 9. As a User I want to get visual feedback from connections that are active (1/on) and ones that are not (0/off).
  * We draw black lines to show off state
  * We draw red lines to show off state
#### 10. As a User I want to be able to include switches in my circuit, so that I can interactively connect/disconnect different components.
  * We create a component that can either transparent the signal or block it (we determine which through code).
  * We add the functionality to change the state by clicking on the switch.
#### 11. As a User I want to have a component that gives a signal source, since I can power my circuits that way.
#### 12. As a User I want to be able to draw leads between components, so that I can connect them in any way that is practical for me.
#### 13. As a User I want to have a pulse clock so that I can produce dynamic circuits.
#### 14. As a User I want to have a access to logic gates such as AND, OR, NOT, XOR, NOR, NAND.
#### 15. As a User I want to have display components so I that can show numbers.
#### 16. As a User I want to have components with various abstraction levels so that I can quickly get started from the abstractions level I'm currently interested in.
