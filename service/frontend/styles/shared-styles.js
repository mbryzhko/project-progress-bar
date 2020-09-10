const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `

<dom-module id="my-progress-bar-theme" theme-for="vaadin-progress-bar">
   <template>
      <style>
         :host(.project-progress-green) [part="value"] {
             background-color: green;
         }

         :host(.project-progress-red) [part="value"] {
             background-color: #ff0000;
         }
         
         :host(.project-progress-yellow) [part="value"] {
             background-color: yellow;
         }
      </style>
   </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);