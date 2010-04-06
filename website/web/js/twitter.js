$(document).ready(function() {
            var s1 = document.createElement('script')
            s1.type = 'text/javascript'
            s1.src = 'http://twitter.com/javascripts/blogger.js'
            var s2 = document.createElement('script')
            s2.type = 'text/javascript'
            s2.src = 'http://twitter.com/statuses/user_timeline/easyinsight.json?callback=twitterCallback2&amp;count=5'
            $("#twitter_div").append(s1)
            $("#twitter_div").append(s2)
})