package com.easyinsight.analysis {
import mx.graphics.GradientEntry;
import mx.graphics.IFill;
import mx.graphics.LinearGradient;
import mx.graphics.SolidColor;

public class FillProvider {

    public static const brightGradients:String = "Bright Gradients";
    public static const warmColors:String = "Warm Colors";
    public static const spectral:String = "Spectral";
    public static const highContrast:String = "High Contrast";

    public static const defaultFill:String = brightGradients;

    public static const fillOptions:Array = [ brightGradients, warmColors, spectral, highContrast];

    public function FillProvider() {
    }

    public static function getColor(scheme:String, defaults:Array, index:int):IFill {
        var array:Array;
        if (scheme == FillProvider.spectral) {
            array = FillProvider.createSpectralColors();
        } else if (scheme == FillProvider.highContrast) {
            array = FillProvider.createSAPColors();
        } else if (scheme == FillProvider.warmColors) {
            array = FillProvider.createWarmColors();
        } else {
            if (defaults != null) {
                array = defaults;
            } else {
                array = FillProvider.createSampleGradient();
            }
        }
        if (array.length == 1) {
            return array[0];
        }
        return array[index % array.length];
    }

    public static function createSpectralColors():Array {
        return [ new SolidColor(0x9E0142), new SolidColor(0xE6F598),
        new SolidColor(0xD53E4F), new SolidColor(0xABDDA4), new SolidColor(0xABDDA4),
        new SolidColor(0xF46D43), new SolidColor(0xFDAE61), new SolidColor(0x66C2A5),
        new SolidColor(0x66C2A5), new SolidColor(0xFEE08B), new SolidColor(0x3288BD),
                new SolidColor(0xFFFFBF)
        ];
    }

    public static function createWarmColors():Array {
        return [ new SolidColor(0x793F0D), new SolidColor(0xAC703D),
        new SolidColor(0xC38E63), new SolidColor(0xE49969), new SolidColor(0xE5AE86),
        new SolidColor(0x6E7649), new SolidColor(0x9D9754), new SolidColor(0xC7C397),
        new SolidColor(0xB4A851), new SolidColor(0xDFD27C), new SolidColor(0xE7E3B5),
                new SolidColor(0x846D74), new SolidColor(0xB7A6AD), new SolidColor(0xD3C9CE)
        ];
    }

    public static function createSAPColors():Array {
        return [ new SolidColor(0xF0B400), new SolidColor(0x1E6C0B),
        new SolidColor(0x00488C), new SolidColor(0x332600), new SolidColor(0xD84000),
        new SolidColor(0x434C43), new SolidColor(0xB00023), new SolidColor(0xF3C01C),
        new SolidColor(0xF8D753), new SolidColor(0xFAE16B), new SolidColor(0xFFF8A3)
        ];
        
        /*
        <mx:SolidColor id="sc1" color="0xF0B400" alpha="1"/>
        <mx:SolidColor id="sc2" color="0x1E6C0B" alpha="1"/>
        <mx:SolidColor id="sc3" color="0x00488C" alpha="1"/>
        <mx:SolidColor id="sc4" color="0x332600" alpha="1"/>
        <mx:SolidColor id="sc5" color="0xD84000" alpha="1"/>
        <mx:SolidColor id="sc6" color="0x434C43" alpha="1"/>
        <mx:SolidColor id="sc7" color="0xB00023" alpha="1"/>

        <mx:SolidColor id="sc8" color="0xF3C01C" alpha="1"/>
        <mx:SolidColor id="sc15" color="0xF8D753" alpha="1"/>
        <mx:SolidColor id="sc22" color="0xFAE16B" alpha="1"/>
        <mx:SolidColor id="sc29" color="0xFFF8A3" alpha="1"/>
         */
    }

    public static function createSampleGradient():Array {
        return [ lg(0xFF0000, 0x550000), lg(0x00FF00, 0x005500), lg(0x0000FF, 0x550000),
            lg(0xFFFF00, 0x555500), lg(0xFF00FF, 0x550055), lg(0x00FFFF, 0x005555),
            lg(0xFFAA00, 0x552200), lg(0xAAFF00, 0x225500), lg(0xFF00AA, 0x550022),
            lg(0xAA00FF, 0x220055), lg(0x00FFAA, 0x005522), lg(0x00AAFF, 0x002255)];
    }

    public static function createSolidColors():Array {
        return [ new SolidColor(0xFF0000), new SolidColor(0x00FF00),
        new SolidColor(0x0000FF), new SolidColor(0xFF00FF), new SolidColor(0x00FFFF),
        new SolidColor(0xFFAA00), new SolidColor(0xAAFF00), new SolidColor(0xFF00AA),
        new SolidColor(0xAA00FF), new SolidColor(0x00FFAA), new SolidColor(0x00AAFF)
        ];
    }

    private static function lg(startColor:uint, endColor:uint):LinearGradient {
        var gradient:LinearGradient = new LinearGradient();
        var startEntry:GradientEntry = new GradientEntry(startColor, 0.0);
        var endEntry:GradientEntry = new GradientEntry(endColor, 1.0);
        gradient.entries = [ startEntry, endEntry ];
        return gradient;
    }
}
}