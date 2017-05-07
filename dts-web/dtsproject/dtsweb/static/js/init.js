$.noConflict();

jQuery( document ).ready(function( $ ) {
  // Code that uses jQuery's $ can follow here.
  // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
  $('.modal-trigger').leanModal();

  $('.button-collapse').sideNav({
     menuWidth: 240, // Default is 240
     edge: 'left', // Choose the horizontal origin
     closeOnClick: false // Closes side-nav on <a> clicks, useful for Angular/Meteor
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

 //ajax csrf token for django
 $.ajaxSetup({
       beforeSend: function(xhr, settings) {
           function getCookie(name) {
               var cookieValue = null;
               if (document.cookie && document.cookie != '') {
                   var cookies = document.cookie.split(';');
                   for (var i = 0; i < cookies.length; i++) {
                       var cookie = jQuery.trim(cookies[i]);
                       // Does this cookie string begin with the name we want?
                       if (cookie.substring(0, name.length + 1) == (name + '=')) {
                           cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                           break;
                       }
                   }
               }
               return cookieValue;
           }
           if (!(/^http:.*/.test(settings.url) || /^https:.*/.test(settings.url))) {
               // Only send the token to relative URLs i.e. locally.
               xhr.setRequestHeader("X-CSRFToken", getCookie('csrftoken'));
           }
       }
  });

  function uploadFile() {
  $.ajax({
      data: $(this).serialize(),
      type: $(this).attr('method'),
      url: $(this).attr('action')
  });
  return false;
  }

  $(function() {
       $('#file-upload-form').submit(uploadFile);
  });

 //uploader
 $("#UploadFile").on("change", function(e) {
  //  var name = $("#af_rpta_propertyland_filename").val();
   var file = e.target.files[0];
   var filename = file.name;
   var filetype = file.type;
   var filesize = file.size;
   var data = {
      //  "filename":filename,
       "filetype":filetype,
       "filesize":filesize
       };
  //  console.log(data);
   var reader = new FileReader();
     reader.onload = function(e) {
       data.file_base64 = e.target.result.split(/,/)[1];
         $.post("/uploadFile", {json:JSON.stringify(data)}, "json")
         .then(function(data) {
          //  console.log(data);
          //  var results = $("<a />", {
          //        "href": "data:" + data.filetype + ";base64," + data.file_base64,
          //        "download": data.filename,
          //        "target": "_blank",
          //        "text": data.filename
          //      });
          //    $("body").append("<br>download: ", results[0]);
         }, function(jqxhr, textStatus, errorThrown) {
           console.log(textStatus, errorThrown)
         })
     };
     reader.readAsDataURL(file)
 });


// jQuery('#pdfContainer').find('.textLayer').annotator();




});
//
//
//
// (function($) {
//
// })(jQuery);
