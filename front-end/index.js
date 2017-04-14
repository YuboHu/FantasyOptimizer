function switchTab(event) {
  var targetId = event.target.id;
  if($('#' + targetId).hasClass('active')) {
    // do nothing
  } else {
    $('li.active').removeClass('active');
    $('#' + targetId).addClass('active');
  }
}