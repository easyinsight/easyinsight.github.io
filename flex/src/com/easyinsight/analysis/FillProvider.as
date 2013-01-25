package com.easyinsight.analysis {
import mx.graphics.GradientEntry;
import mx.graphics.IFill;
import mx.graphics.LinearGradient;
import mx.graphics.RadialGradient;
import mx.graphics.SolidColor;

public class FillProvider {

    public static const linearGradients:String = "Linear Gradients";
    public static const radialGradients:String = "Radial Gradients";
    public static const highContrast:String = "High Contrast";
    public static const ocean:String = "Ocean";

    public static const defaultFill:String = linearGradients;

    public function FillProvider() {
    }

    public static function getColors(scheme:String, angle:int = 0):Array {
        var array:Array;
        if (scheme == FillProvider.highContrast) {
            array = FillProvider.createSAPColors();
        } else if (scheme == FillProvider.linearGradients) {
            array = FillProvider.createLinearGradients(angle);
        } else if (scheme == FillProvider.radialGradients) {
            array = FillProvider.createRadialGradients();
        } else {
            array = FillProvider.createLinearGradients(angle);
        }
        return array;
    }

    public static function getColor(scheme:String, index:int, angle:int = 0):IFill {
        var array:Array;
        if (scheme == FillProvider.highContrast) {
            array = FillProvider.createSAPColors();
        } else if (scheme == FillProvider.linearGradients) {
            array = FillProvider.createLinearGradients(angle);
        } else if (scheme == FillProvider.radialGradients) {
            array = FillProvider.createRadialGradients();
        } else {
            array = FillProvider.createLinearGradients(angle);
        }
        if (array.length == 1) {
            return array[0];
        }
        return array[index % array.length];
    }

    public static function createSAPColors():Array {
        return [ new SolidColor(0xF0B400), new SolidColor(0x1E6C0B),
        new SolidColor(0x00488C), new SolidColor(0x332600), new SolidColor(0xD84000),
        new SolidColor(0x434C43), new SolidColor(0xB00023), new SolidColor(0xF3C01C),
        new SolidColor(0xF8D753), new SolidColor(0xFAE16B), new SolidColor(0xFFF8A3)
        ];
    }

    public static function createLinearGradients(angle:int):Array {
        return [ lg(0xf9fbf5, 0xa6bc59, angle), lg(0xf1f3f7, 0x597197, angle), lg(0xf9f3e0, 0xd6ab2a, angle),
        lg(0xfcf3ef, 0xd86068, angle), lg(0xe9f1e5, 0x5d9942, angle), lg(0xd4c5cd, 0x7a4c6c, angle),
        lg(0xffffff, 0x61B796, angle), lg(0xffffff, 0x3a93e8, angle), lg(0xffffff, 0xAB3032, angle),
        lg(0xffffff, 0x377026, angle), lg(0xffffff, 0x8e3160, angle), lg(0xffffff, 0x5a46cc, angle),
        lg(0xffffff, 0xBBC417, angle), lg(0xffffff, 0x2921B8, angle), lg(0xffffff, 0xB57C99, angle),
        lg(0xffffff, 0xB57C99, angle)];
        //return [new SolidColor(0xB22222), new SolidColor(0xDC143C), new SolidColor(0xCD5C5C), new SolidColor(0xF08080), new SolidColor(0xE9967A), new SolidColor(0xFA8072), new SolidColor(0xFFA07A)];
        //return [new SolidColor(0x8db6c7), new SolidColor(0xc1b3be), new SolidColor(0xd1c6bf), new SolidColor(0xca9f92), new SolidColor(0xf9cd97), new SolidColor(0xe3d9b0), new SolidColor(0xb1c27a), new SolidColor(0xb2e289), new SolidColor(0xf1c0bf), new SolidColor(0x59add0), new SolidColor(0x7095e1), new SolidColor(0x9fa3e3), new SolidColor(0xc993d4), new SolidColor(0xdb8db2), new SolidColor(0xf1c3d0)];
    }

    public static function createRadialGradients2():Array {
        return [ cg(0xa6bc59, 0xa6bc59)  // green
            , cg(0x597197, 0x597197), // blue
            cg(0xd6ab2a, 0xd6ab2a), // yellow
            cg(0xd86068, 0xd86068),               // red
            cg(0x5d9942, 0x5d9942),  // green
            cg(0x7a4c6c, 0x7a4c6c),           // purple
            cg(0x889949, 0x889949), // green
            cg(0x38517A, 0x38517A), // dark blue

            cg(0xBC8F08, 0xBC8F08), // yellow
            cg(0x944248, 0x944248), // red
            cg(0x447030, 0x447030), // dark green
            cg(0x56354C, 0x56354C) // dark purple
        ];
    }

    public static function createRadialGradients():Array {
        return [ cg(0xf9fbf5, 0xa6bc59)  // green
            , cg(0xf1f3f7, 0x597197), // blue
            cg(0xf9f3e0, 0xd6ab2a), // yellow
        cg(0xfcf3ef, 0xd86068),               // red
            cg(0xe9f1e5, 0x5d9942),  // green
            cg(0xd4c5cd, 0x7a4c6c),           // purple
        cg(0xffffff, 0x889949), // green
                cg(0xffffff, 0x38517A), // dark blue

        cg(0xffffff, 0xBC8F08), // yellow
            cg(0xffffff, 0x944248), // red
                cg(0xffffff, 0x447030), // dark green
            cg(0xffffff, 0x56354C) // dark purple
        ];
        /*return [cg(0xffffff, 0xA6BC59), cg(0xffffff, 0x59BC66), cg(0xffffff, 0x59BCB7),
        cg(0xffffff, 0x598EBC), cg(0xffffff, 0x7D59BC)];*/
    }

    public static function createSolidColors():Array {
        return [ new SolidColor(0xa6bc59), new SolidColor(0x597197),
        new SolidColor(0xd6ab2a), new SolidColor(0xd86068), new SolidColor(0x5d9942),
        new SolidColor(0x7a4c6c), new SolidColor(0xF0B400), new SolidColor(0x1E6C0B),
        new SolidColor(0x00488C), new SolidColor(0x332600), new SolidColor(0xD84000)
        ];
    }

    private static function lg(startColor:uint, endColor:uint, angle:int = 0):LinearGradient {
        var gradient:LinearGradient = new LinearGradient();
        gradient.angle = angle;
        var entry1:GradientEntry = new GradientEntry(endColor, 0.0);
        var entry2:GradientEntry = new GradientEntry(startColor, 0.15);
        var entry3:GradientEntry = new GradientEntry(endColor, .5);
        var entry4:GradientEntry = new GradientEntry(endColor, .9);
        var entry5:GradientEntry = new GradientEntry(startColor, 1);
        gradient.entries = [ entry1, entry2, entry3, entry4, entry5 ];
        return gradient;
    }

    private static function cg(startColor:uint, endColor:uint):IFill {
        var gradient:RadialGradient = new RadialGradient();
        gradient.angle = 90;
        /*var entry1:GradientEntry = new GradientEntry(startColor, 0.0);
        var entry2:GradientEntry = new GradientEntry(endColor, .3);*/
        var entry3:GradientEntry = new GradientEntry(endColor, .7);
        var entry4:GradientEntry = new GradientEntry(startColor, .9);
        var entry5:GradientEntry = new GradientEntry(endColor, 1);
        gradient.entries = [ entry3, entry4, entry5 ];
        return gradient;
    }
}
}