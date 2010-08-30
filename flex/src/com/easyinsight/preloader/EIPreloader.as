package com.easyinsight.preloader {
import flash.display.DisplayObject;
    import flash.display.GradientType;
    import flash.display.Sprite;
    import flash.events.Event;
    import flash.events.ProgressEvent;
    import flash.filters.DropShadowFilter;
    import flash.geom.Matrix;
    import flash.text.TextField;
    import flash.text.TextFormat;

    import mx.events.FlexEvent;
    import mx.preloaders.IPreloaderDisplay;

    /**
     * This custom preloader is just one more copy of the copy of the copy
     *
     * This custom preloader comes from:
     *         http://flexdevtips.blogspot.com/2009/03/another-custom-preloader.html
     * That was coming from:
     *         http://www.pathf.com/blogs/2008/08/custom-flex-3-lightweight-preloader-with-source-code/
     * That... was based on:
     *         https://defiantmouse.com/yetanotherforum.net/Default.aspx?g=posts&t=82
     * That was inspired by:
     *         http://ted.onflash.org/2006/07/flex-2-preloaders-swf-png-gif-examples.php
     * (I hope I didn't forget anyone!)
     *
     * This custom preloader is:
     *         - filling the background of the whole stage (with bgColors)
     *         - drawing a progress box with a blue background (defined by boxColors) containing:
     *             . the logo (embedded below)
     *             . a progress bar with a lighter blue gradient (defined by barColors)
     *               with a colored border (defined by barFrameColor)
     *             . the progress indication
     *
     * For a basic usage:
     *         Add this class to your application
     *         Replace preloaderLogo.png by yours
     *      Change the colors
     *         Add preloader="CustomPreloaderSmall" in your Application definition
     *         And you're done!
     *
     * If you want more customization, extend this class to customize the colors, the logo or the font size
     * (by chaging the corresponding properties), or make bigger changes by overriding the methods
     * used for rendering or for progress processing.
     *
     * @author JYC
     */
    public class EIPreloader extends Sprite implements IPreloaderDisplay
    {
        // Embed the Logo
        [Embed("../../../../assets/large_logo2.PNG")]
        [Bindable]
        protected var LogoClass:Class;

        /**
         * logo as a DisplayObject
         * @default
         */
        private var logo:DisplayObject;

        private var _ready:Boolean = false;
        private var _bytesLoaded:uint = 0;
        private var _bytesExpected:uint = 1; // we start at 1 to avoid division by zero errors.
        private var _progressRate:Number = 0;
        private var _preloader:Sprite;

        private var smallDropShadow:DropShadowFilter = new DropShadowFilter(2, 45, 0x000000, 0.5);
        //private var largeDropShadow:DropShadowFilter = new DropShadowFilter(6, 45, 0x333333, 0.9);

        // mainBox containing the logo & the progress bar
        private var mainBox:Sprite;
        // progress bar sprite
        private var bar:Sprite;

        // frame around the progress bar
        private var barFrame:Sprite;

        // textfield for rendering the progress
        private var progressTextField:TextField;

        // matrix for the progress bar gradient
        private var progressBarMatrix:Matrix;

        /**
         * Background color for the stage
         * @default Black: [0x0]
         */
        protected var bgColors:Array = [0xFFFFFF];
        //protected var bgColors:Array = [0x389CFF, 0x0];

        /**
         * Main box background color
         * @default Gradient: [0x4184d4, 0x14479a]
         */
        protected var boxColors:Array = [0xFFFFFF, 0xFFFFFF];

        /**
         * Progress bar color
         * @default Gradient: [0x95b7e7, 0x6e99d5, 0x1379fb, 0x2d9be8]
         */
        protected var barColors:Array = [0x656161, 0x656161, 0x656161, 0x656161];

        /**
         * Progress bar frame color
         * @default 0xdddddd
         */
        protected var barFrameColor:uint = 0x454141;

        /**
         * Progress bar rounded corner radius
         * @default 0
         */
        protected var barRadius:int = 0;

        /**
         * Progress indication text font
         * @default Verdana
         */
        protected var progressFont:String = "Verdana";

        /**
         * Progress indication text color
         * @default 0xffffff
         */
        protected var progressColor:uint = 0x656161;

        /**
         * String displayed in front of the progress rate
         * @default Loading
         */
        protected var loading:String = "Loading ";

        /**
         * Progress bar width (is also the main box minimum width)
         * @default 100
         */
        protected var barWidth:int = 100;

        /**
         * Progress bar height (adapt the font size when changing this)
         * @default 15
         */
        protected var barHeight:int = 15;

        /**
         * Horizontal margin around logo & progress bar
         * @default 10
         */
        protected var mainBoxHorizontalMargin:Number = 10;

        /**
         * Vertical margin around logo & progress bar
         * @default 10
         */
        protected var mainBoxVerticalMargin:Number = 10;

        /**
         * Space between bottom of Logo and top of Progress bar
         * @default 5
         */
        protected var logoProgressBarSpacing:Number = 5;

        /**
         * Font size of progress indicator (must be in sync with barHeight)
         * @default 9
         */
        protected var progressFontSize:uint = 14;

        /**
         * Constructor
         */
        public function EIPreloader()
        {
            super();
        }

        /**
         * Preloader is almost initialized.
         *
         * In some cases, the stage width / height are still unavailable at that point,
         * as a workaround, listen to ENTER_FRAME events until stageWidth/Height are defined.
         */
        public function initialize():void
        {
            addEventListener(Event.ENTER_FRAME, waitForStageWidth);
        }

        /**
         * Wait for stage width to be available (stageWidth is initially returned as 0 with some browsers).
         * @private
         */

        private function waitForStageWidth(e:Event):void
        {
            if (stage && stage.stageWidth != 0)
            {
                _ready = true;
                removeEventListener(Event.ENTER_FRAME, waitForStageWidth);
                init();
            }
        }

        /**
         * Preloader is now initialized.
         *
         * Fill the background and create all elements
         */
        protected function init():void
        {
            drawBackground();
            createAssets();
        }

        /**
         * Draw the stage background using <code>bgColors</code>
         */
        protected function drawBackground():void
        {
            if (bgColors.length == 2)
            {
                var matrix:Matrix = new Matrix();
                matrix.createGradientBox(stageWidth, stageHeight, Math.PI / 2);
                graphics.beginGradientFill(GradientType.LINEAR, bgColors, [1, 1], [0, 255], matrix);
            }
            else
            {
                graphics.beginFill(bgColors[0]);
            }
            graphics.drawRect(0, 0, stageWidth, stageHeight);
            graphics.endFill();
        }

        /**
         * Create the different elements: logo, progress bar and its frame, progressTextField.
         */
        protected function createAssets():void
        {
            // load the logo first to retrieve its dimensions
            if (!logo) {
                logo = new LogoClass();
            }
            var logoWidth:Number = logo.width;
            var logoHeight:Number = logo.height;

            // Create and dimension the Progress Bar
            bar = new Sprite();
            // make the progress bar the same width as the logo if the logo is larger than default bar width
            barWidth = Math.max(barWidth, logoWidth);

            // calculate the box size & add margins
            var mainBoxWidth:Number = Math.max(logoWidth, barWidth) + mainBoxHorizontalMargin * 2;
            var mainBoxHeight:Number = logoHeight + barHeight + logoProgressBarSpacing + mainBoxVerticalMargin * 2;

            // create and center the main box (all other sprites are added to it)
            mainBox = new Sprite();
            if (stage) {
                mainBox.x = stage.stageWidth / 2 - mainBoxWidth / 2;
                mainBox.y = stage.stageHeight / 2 - mainBoxHeight / 2;
            }

            // fill the main box with boxColors and add it to the stage
            //mainBox.filters = [largeDropShadow];
            var matrix:Matrix = new Matrix();
            matrix.createGradientBox(mainBoxWidth, mainBoxHeight, Math.PI / 2);
            mainBox.graphics.beginGradientFill(GradientType.LINEAR, boxColors, [1, 1], [0, 255], matrix);
            mainBox.graphics.drawRoundRectComplex(0, 0, mainBoxWidth, mainBoxHeight, 12, 0, 0, 12);
            mainBox.graphics.endFill();
            addChild(mainBox);

            // position the logo
            logo.x = mainBoxHorizontalMargin;
            logo.y = mainBoxVerticalMargin;
            mainBox.addChild(logo);

            // create and position the progress bar
            bar = new Sprite();
            bar.graphics.drawRoundRect(0, 0, barWidth, barHeight, barRadius, barRadius);
            bar.x = mainBoxHorizontalMargin;
            bar.y = logo.y + logoHeight + logoProgressBarSpacing;
            mainBox.addChild(bar);

            // create and position the progress bar frame
            barFrame = new Sprite();
            barFrame.graphics.lineStyle(1, barFrameColor, 1)
            barFrame.graphics.drawRoundRect(0, 0, barWidth, barHeight, barRadius, barRadius);
            barFrame.graphics.endFill();
            barFrame.x = bar.x;
            barFrame.y = bar.y;
            barFrame.filters = [smallDropShadow];
            mainBox.addChild(barFrame);

            //create the progress text field (to show percentage of loading), centered over the progress bar
            progressTextField = new TextField();
            // setup the text font, color, and center alignment
            var tf:TextFormat = new TextFormat(progressFont, progressFontSize, progressColor, true, null, null, null, null, "center");
            progressTextField.defaultTextFormat = tf;
            // set the text AFTER the textformat has been set, otherwise the text sizes are wrong
            progressTextField.text = loading + " 0%";
            progressTextField.width = barWidth;
            // important - give the textfield a proper height
            progressTextField.height = progressTextField.textHeight + 6;

            progressTextField.x = bar.x;
            progressTextField.y = bar.y + Math.round((bar.height - progressTextField.height) / 2) + 40;
            mainBox.addChild(progressTextField);
        }

        /**
         * Refresh the loading percentage.
         *
         * Called whenever the preloader provides a new progress.
         */
        protected function draw():void
        {
            // Wait for correct initialization
            if (!_ready)
                return;

            // update the % progress
            progressTextField.text = loading + Math.round(_progressRate * 100).toString() + "%";

            // draw a gradient progress bar
            if (!progressBarMatrix) {
                // Create the gradient matrix during first rendering
                progressBarMatrix = new Matrix();
                progressBarMatrix.createGradientBox(bar.width, bar.height, Math.PI / 2);
            }
            bar.graphics.beginGradientFill(GradientType.LINEAR, barColors, [1, 1, 1, 1], [0, 127, 128, 255], progressBarMatrix);
            bar.graphics.drawRoundRect(0, 0, bar.width * _progressRate, bar.height, barRadius, barRadius);
            bar.graphics.endFill();
        }

        /**
         * Preloader is now defined, listen to its progress events.
         * @param value preloader sprite
         */
        public function set preloader(value:Sprite):void
        {
            _preloader = value;
            _preloader.addEventListener(ProgressEvent.PROGRESS, progressHandler);
            _preloader.addEventListener(Event.COMPLETE, completeHandler);
            _preloader.addEventListener(FlexEvent.INIT_PROGRESS, initProgressHandler);
            _preloader.addEventListener(FlexEvent.INIT_COMPLETE, initCompleteHandler);
        }

        /**
         * IPreloaderDisplay interface implementation
         * @param alpha
         */
        public function set backgroundAlpha(alpha:Number):void
        {
        }

        /**
         *
         * @private
         */
        public function get backgroundAlpha():Number
        {
            return 1;
        }

        private var _backgroundColor:uint = 0xffffffff;

        /**
         * IPreloaderDisplay interface implementation
         * @param color
         */
        public function set backgroundColor(color:uint):void
        {
            _backgroundColor = color;
        }

        /**
         *
         * @private
         */
        public function get backgroundColor():uint
        {
            return _backgroundColor;
        }

        /**
         * IPreloaderDisplay interface implementation
         * @param image
         */
        public function set backgroundImage(image:Object):void
        {
        }

        /**
         *
         * @private
         */
        public function get backgroundImage():Object
        {
            return null;
        }

        /**
         * IPreloaderDisplay interface implementation
         * @param size
         */
        public function set backgroundSize(size:String):void
        {
        }

        /**
         *
         * @private
         */
        public function get backgroundSize():String
        {
            return "auto";
        }

        private var _stageHeight:Number = 300;

        /**
         * IPreloaderDisplay interface implementation
         * @param height
         */
        public function set stageHeight(height:Number):void
        {
            _stageHeight = height;
        }

        /**
         *
         * @private
         */
        public function get stageHeight():Number
        {
            return _stageHeight;
        }

        private var _stageWidth:Number = 400;

        /**
         * IPreloaderDisplay interface implementation
         * @param width
         */
        public function set stageWidth(width:Number):void
        {
            _stageWidth = width;
        }

        /**
         *
         * @private
         */
        public function get stageWidth():Number
        {
            return _stageWidth;
        }

        /**
         * Called as the download progresses
         * @param event
         */
        protected function progressHandler(event:ProgressEvent):void
        {
            // Update download progress
            _bytesLoaded = event.bytesLoaded;
            _bytesExpected = event.bytesTotal;
            _progressRate = Number(_bytesLoaded) / Number(_bytesExpected);

            // Refresh
            draw();
        }

        /**
         * Called when the download is complete, initialization will now start
         * @param event
         */
        protected function completeHandler(event:Event):void
        {
        }

        /**
         * Called as the initialization progresses
         * @param event
         */
        protected function initProgressHandler(event:Event):void
        {
            draw();
        }

        /**
         * Called when initialization is complete
         * Complete the preload phase by triggering <code>Event.COMPLETE</code>.
         * @param event
         */
        protected function initCompleteHandler(event:Event):void
        {
            // Remove our listeners
            _preloader.removeEventListener(ProgressEvent.PROGRESS, progressHandler);
            _preloader.removeEventListener(Event.COMPLETE, completeHandler);
            _preloader.removeEventListener(FlexEvent.INIT_PROGRESS, initProgressHandler);
            _preloader.removeEventListener(FlexEvent.INIT_COMPLETE, initCompleteHandler);
            // And complete the preload phase
            dispatchEvent(new Event(Event.COMPLETE));
        }
    }

}