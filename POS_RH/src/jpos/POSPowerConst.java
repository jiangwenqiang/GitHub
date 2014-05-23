/////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives.Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted.
//
// POSPowerConst
//
//   POSPower constants for JavaPOS Applications.
//
// Modification history
// ------------------------------------------------------------------
// 2000-Apr-18 JavaPOS Release 1.5                                 BS
//
/////////////////////////////////////////////////////////////////////

package jpos;

public interface POSPowerConst
{
    /////////////////////////////////////////////////////////////////////
    // "CapUPSChargeState" Capability and "UPSChargeState" Property
    //    Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PWR_UPS_FULL      = 0x00000001;
    public static final int PWR_UPS_WARNING   = 0x00000002;
    public static final int PWR_UPS_LOW       = 0x00000004;
    public static final int PWR_UPS_CRITICAL  = 0x00000008;


    /////////////////////////////////////////////////////////////////////
    // Status Update Event: "Status" Parameter Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PWR_SUE_UPS_FULL            = 11;
    public static final int PWR_SUE_UPS_WARNING         = 12;
    public static final int PWR_SUE_UPS_LOW             = 13;
    public static final int PWR_SUE_UPS_CRITICAL        = 14;
    public static final int PWR_SUE_FAN_STOPPED         = 15;
    public static final int PWR_SUE_FAN_RUNNING         = 16;
    public static final int PWR_SUE_TEMPERATURE_HIGH    = 17;
    public static final int PWR_SUE_TEMPERATURE_OK      = 18;
    public static final int PWR_SUE_SHUTDOWN            = 19;
}