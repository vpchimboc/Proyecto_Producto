/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import rojerusan.RSNotifyAnimated;

/**
 *
 * @author 59399
 */
public class Resouces {

    public static void success(String actividad, String mensaje) {
        new rojerusan.RSNotifyAnimated(actividad, mensaje,
                5, RSNotifyAnimated.PositionNotify.BottomRight, RSNotifyAnimated.AnimationNotify.RightLeft,
                RSNotifyAnimated.TypeNotify.SUCCESS).setVisible(true);
    }

    public static void warning(String actividad, String mensaje) {
        new rojerusan.RSNotifyAnimated(actividad, mensaje,
                5, RSNotifyAnimated.PositionNotify.BottomRight, RSNotifyAnimated.AnimationNotify.RightLeft,
                RSNotifyAnimated.TypeNotify.WARNING).setVisible(true);
    }

    public static void error(String actividad, String mensaje) {
        new rojerusan.RSNotifyAnimated(actividad, mensaje,
                5, RSNotifyAnimated.PositionNotify.BottomRight, RSNotifyAnimated.AnimationNotify.RightLeft,
                RSNotifyAnimated.TypeNotify.ERROR).setVisible(true);
    }


}
