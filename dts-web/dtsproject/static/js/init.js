(function($){
  $(function(){


    // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
    $('.modal-trigger').leanModal();

    $('.button-collapse').sideNav({
       menuWidth: 240, // Default is 240
       edge: 'left', // Choose the horizontal origin
       closeOnClick: true // Closes side-nav on <a> clicks, useful for Angular/Meteor
     }
   );

   $('.dropdown-button').dropdown({
     inDuration: 300,
     outDuration: 225,
     constrain_width: false, // Does not change width of dropdown to that of the activator
     hover: true, // Activate on hover
     gutter: 10, // Spacing from edge
     belowOrigin: true, // Displays dropdown below the button
     alignment: 'left' // Displays dropdown with edge aligned to the left of button
   }
 );

  }); // end of document ready
})(jQuery); // end of jQuery name space
