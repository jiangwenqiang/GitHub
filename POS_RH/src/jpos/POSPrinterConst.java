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
// POSPrinterConst
//
//   POS Printer constants for JavaPOS Applications.
//
// Modification history
// ------------------------------------------------------------------
// 98-02-18 JavaPOS Release 1.2                                   BS
// 98-04-20 JavaPOS Release 1.3                                   BS
//   Add more PrintTwoNormal constants.
//
/////////////////////////////////////////////////////////////////////

package jpos;

public interface POSPrinterConst
{
    //###################################################################
    //#### POS Printer Constants
    //###################################################################

    /////////////////////////////////////////////////////////////////////
    // Printer Station Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_S_JOURNAL        = 1;
    public static final int PTR_S_RECEIPT        = 2;
    public static final int PTR_S_SLIP           = 4;

    public static final int PTR_S_JOURNAL_RECEIPT= PTR_S_JOURNAL | PTR_S_RECEIPT;
    public static final int PTR_S_JOURNAL_SLIP   = PTR_S_JOURNAL | PTR_S_SLIP   ;
    public static final int PTR_S_RECEIPT_SLIP   = PTR_S_RECEIPT | PTR_S_SLIP   ;

    public static final int PTR_TWO_RECEIPT_JOURNAL  = 0x8000 + PTR_S_JOURNAL_RECEIPT;
    public static final int PTR_TWO_SLIP_JOURNAL     = 0x8000 + PTR_S_JOURNAL_SLIP   ;
    public static final int PTR_TWO_SLIP_RECEIPT     = 0x8000 + PTR_S_RECEIPT_SLIP   ;


    /////////////////////////////////////////////////////////////////////
    // "CapCharacterSet" Property Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_CCS_ALPHA        =   1;
    public static final int PTR_CCS_ASCII        = 998;
    public static final int PTR_CCS_KANA         =  10;
    public static final int PTR_CCS_KANJI        =  11;
    public static final int PTR_CCS_UNICODE      = 997;


    /////////////////////////////////////////////////////////////////////
    // "CharacterSet" Property Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_CS_UNICODE       = 997;
    public static final int PTR_CS_ASCII         = 998;
    public static final int PTR_CS_ANSI          = 999;


    /////////////////////////////////////////////////////////////////////
    // "ErrorLevel" Property Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_EL_NONE          = 1;
    public static final int PTR_EL_RECOVERABLE   = 2;
    public static final int PTR_EL_FATAL         = 3;


    /////////////////////////////////////////////////////////////////////
    // "MapMode" Property Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_MM_DOTS          = 1;
    public static final int PTR_MM_TWIPS         = 2;
    public static final int PTR_MM_ENGLISH       = 3;
    public static final int PTR_MM_METRIC        = 4;


		/////////////////////////////////////////////////////////////////////
		// "CapXxxColor" Property Constants
		/////////////////////////////////////////////////////////////////////

    public static final int PTR_COLOR_PRIMARY      = 0x00000001;
    public static final int PTR_COLOR_CUSTOM1      = 0x00000002;
    public static final int PTR_COLOR_CUSTOM2      = 0x00000004;
    public static final int PTR_COLOR_CUSTOM3      = 0x00000008;
    public static final int PTR_COLOR_CUSTOM4      = 0x00000010;
    public static final int PTR_COLOR_CUSTOM5      = 0x00000020;
    public static final int PTR_COLOR_CUSTOM6      = 0x00000040;
    public static final int PTR_COLOR_CYAN         = 0x00000100;
    public static final int PTR_COLOR_MAGENTA      = 0x00000200;
    public static final int PTR_COLOR_YELLOW       = 0x00000400;
    public static final int PTR_COLOR_FULL         = 0x80000000;


		/////////////////////////////////////////////////////////////////////
		// "CapXxxCartridgeSensor" and  "XxxCartridgeState" Property Constants
		/////////////////////////////////////////////////////////////////////

    public static final int PTR_CART_UNKNOWN         = 0x10000000;
    public static final int PTR_CART_OK              = 0x00000000;
    public static final int PTR_CART_REMOVED         = 0x00000001;
    public static final int PTR_CART_EMPTY           = 0x00000002;
    public static final int PTR_CART_NEAREND         = 0x00000004;
    public static final int PTR_CART_CLEANING        = 0x00000008;


		/////////////////////////////////////////////////////////////////////
		// "CartridgeNotify"  Property Constants
		/////////////////////////////////////////////////////////////////////

    public static final int PTR_CN_DISABLED        = 0x00000000;
    public static final int PTR_CN_ENABLED         = 0x00000001;


    /////////////////////////////////////////////////////////////////////
    // "CutPaper" Method Constant
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_CP_FULLCUT       = 100;


    /////////////////////////////////////////////////////////////////////
    // "PrintBarCode" Method Constants:
    /////////////////////////////////////////////////////////////////////

    //   "Alignment" Parameter
    //     Either the distance from the left-most print column to the start
    //     of the bar code, or one of the following:

    public static final int PTR_BC_LEFT          = -1;
    public static final int PTR_BC_CENTER        = -2;
    public static final int PTR_BC_RIGHT         = -3;

    //   "TextPosition" Parameter

    public static final int PTR_BC_TEXT_NONE     = -11;
    public static final int PTR_BC_TEXT_ABOVE    = -12;
    public static final int PTR_BC_TEXT_BELOW    = -13;

    //   "Symbology" Parameter:

    //     One dimensional symbologies
    public static final int PTR_BCS_UPCA         = 101;  // Digits
    public static final int PTR_BCS_UPCE         = 102;  // Digits
    public static final int PTR_BCS_JAN8         = 103;  // = EAN 8
    public static final int PTR_BCS_EAN8         = 103;  // = JAN 8 (added in 1.2)
    public static final int PTR_BCS_JAN13        = 104;  // = EAN 13
    public static final int PTR_BCS_EAN13        = 104;  // = JAN 13 (added in 1.2)
    public static final int PTR_BCS_TF           = 105;  // (Discrete 2 of 5) Digits
    public static final int PTR_BCS_ITF          = 106;  // (Interleaved 2 of 5) Digits
    public static final int PTR_BCS_Codabar      = 107;  // Digits, -, $, :, /, ., +;
                                                         //   4 start/stop characters
                                                         //   (a, b, c, d)
    public static final int PTR_BCS_Code39       = 108;  // Alpha, Digits, Space, -, .,
                                                         //   $, /, +, %; start/stop (*)
                                                         // Also has Full ASCII feature
    public static final int PTR_BCS_Code93       = 109;  // Same characters as Code 39
    public static final int PTR_BCS_Code128      = 110;  // 128 data characters
    //        (The following were added in Release 1.2)
    public static final int PTR_BCS_UPCA_S       = 111;  // UPC-A with supplemental
                                                         //   barcode
    public static final int PTR_BCS_UPCE_S       = 112;  // UPC-E with supplemental
                                                         //   barcode
    public static final int PTR_BCS_UPCD1        = 113;  // UPC-D1
    public static final int PTR_BCS_UPCD2        = 114;  // UPC-D2
    public static final int PTR_BCS_UPCD3        = 115;  // UPC-D3
    public static final int PTR_BCS_UPCD4        = 116;  // UPC-D4
    public static final int PTR_BCS_UPCD5        = 117;  // UPC-D5
    public static final int PTR_BCS_EAN8_S       = 118;  // EAN 8 with supplemental
                                                         //   barcode
    public static final int PTR_BCS_EAN13_S      = 119;  // EAN 13 with supplemental
                                                         //   barcode
    public static final int PTR_BCS_EAN128       = 120;  // EAN 128
    public static final int PTR_BCS_OCRA         = 121;  // OCR "A"
    public static final int PTR_BCS_OCRB         = 122;  // OCR "B"


    //     Two dimensional symbologies
    public static final int PTR_BCS_PDF417       = 201;
    public static final int PTR_BCS_MAXICODE     = 202;

    //     Start of Printer-Specific bar code symbologies
    public static final int PTR_BCS_OTHER        = 501;


    /////////////////////////////////////////////////////////////////////
    // "PrintBitmap" Method Constants:
    /////////////////////////////////////////////////////////////////////

    //   "Width" Parameter
    //     Either bitmap width or:

    public static final int PTR_BM_ASIS          = -11;  // One pixel per printer dot

    //   "Alignment" Parameter
    //     Either the distance from the left-most print column to the start
    //     of the bitmap, or one of the following:

    public static final int PTR_BM_LEFT          = -1;
    public static final int PTR_BM_CENTER        = -2;
    public static final int PTR_BM_RIGHT         = -3;


    /////////////////////////////////////////////////////////////////////
    // "RotatePrint" Method: "Rotation" Parameter Constants
    // "RotateSpecial" Property Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_RP_NORMAL        = 0x0001;
    public static final int PTR_RP_RIGHT90       = 0x0101;
    public static final int PTR_RP_LEFT90        = 0x0102;
    public static final int PTR_RP_ROTATE180     = 0x0103;

		// Version 1.7. One of the following values can be
		// ORed with one of the above values.
    public static final int PTR_RP_BARCODE       = 0x1000;
    public static final int PTR_RP_BITMAP        = 0x2000;


    /////////////////////////////////////////////////////////////////////
    // "SetLogo" Method: "Location" Parameter Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_L_TOP            = 1;
    public static final int PTR_L_BOTTOM         = 2;


    /////////////////////////////////////////////////////////////////////
    // "TransactionPrint" Method: "Control" Parameter Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_TP_TRANSACTION   = 11;
    public static final int PTR_TP_NORMAL        = 12;


		/////////////////////////////////////////////////////////////////////
		// "MarkFeed" Method: "Type" Parameter Constants
		// "CapRecMarkFeed" Property Constants
		/////////////////////////////////////////////////////////////////////

    public static final int PTR_MF_TO_TAKEUP      = 1;
    public static final int PTR_MF_TO_CUTTER      = 2;
    public static final int PTR_MF_TO_CURRENT_TOF = 4;
    public static final int PTR_MF_TO_NEXT_TOF    = 8;


		/////////////////////////////////////////////////////////////////////
		// "ChangePrintSide" Method: "Side" Parameter Constants
		/////////////////////////////////////////////////////////////////////

    public static final int PTR_PS_UNKNOWN  = 0;
    public static final int PTR_PS_SIDE1    = 1;
    public static final int PTR_PS_SIDE2    = 2;
    public static final int PTR_PS_OPPOSITE = 3;


    /////////////////////////////////////////////////////////////////////
    // "StatusUpdateEvent" Event: "status" Parameter Constants
    /////////////////////////////////////////////////////////////////////

    public static final int PTR_SUE_COVER_OPEN   =   11;
    public static final int PTR_SUE_COVER_OK     =   12;

    public static final int PTR_SUE_JRN_EMPTY    =   21;
    public static final int PTR_SUE_JRN_NEAREMPTY=   22;
    public static final int PTR_SUE_JRN_PAPEROK  =   23;

    public static final int PTR_SUE_REC_EMPTY    =   24;
    public static final int PTR_SUE_REC_NEAREMPTY=   25;
    public static final int PTR_SUE_REC_PAPEROK  =   26;

    public static final int PTR_SUE_SLP_EMPTY    =   27;
    public static final int PTR_SUE_SLP_NEAREMPTY=   28;
    public static final int PTR_SUE_SLP_PAPEROK  =   29;

    public static final int PTR_SUE_JRN_CARTRIDGE_EMPTY     = 41;
    public static final int PTR_SUE_JRN_CARTRIDGE_NEAREMPTY = 42;
    public static final int PTR_SUE_JRN_HEAD_CLEANING       = 43;
    public static final int PTR_SUE_JRN_CARTDRIGE_OK        = 44;

    public static final int PTR_SUE_REC_CARTRIDGE_EMPTY     = 45;
    public static final int PTR_SUE_REC_CARTRIDGE_NEAREMPTY = 46;
    public static final int PTR_SUE_REC_HEAD_CLEANING       = 47;
    public static final int PTR_SUE_REC_CARTDRIGE_OK        = 48;

    public static final int PTR_SUE_SLP_CARTRIDGE_EMPTY     = 49;
    public static final int PTR_SUE_SLP_CARTRIDGE_NEAREMPTY = 50;
    public static final int PTR_SUE_SLP_HEAD_CLEANING       = 51;
    public static final int PTR_SUE_SLP_CARTDRIGE_OK        = 52;

    public static final int PTR_SUE_IDLE         = 1001;


    /////////////////////////////////////////////////////////////////////
    // "ResultCodeExtended" Property Constants for Printer
    /////////////////////////////////////////////////////////////////////

    public static final int JPOS_EPTR_COVER_OPEN = 1 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_JRN_EMPTY  = 2 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_REC_EMPTY  = 3 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_SLP_EMPTY  = 4 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_SLP_FORM   = 5 + JposConst.JPOSERREXT; // EndRemoval
    public static final int JPOS_EPTR_TOOBIG     = 6 + JposConst.JPOSERREXT; // PrintBitmap
    public static final int JPOS_EPTR_BADFORMAT  = 7 + JposConst.JPOSERREXT; // PrintBitmap
    public static final int JPOS_EPTR_JRN_CARTRIDGE_REMOVED = 8 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_JRN_CARTRIDGE_EMPTY   = 9 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_JRN_HEAD_CLEANING     = 10 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_REC_CARTRIDGE_REMOVED = 11 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_REC_CARTRIDGE_EMPTY   = 12 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_REC_HEAD_CLEANING     = 13 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_SLP_CARTRIDGE_REMOVED = 14 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_SLP_CARTRIDGE_EMPTY   = 15 + JposConst.JPOSERREXT; // (Several)
    public static final int JPOS_EPTR_SLP_HEAD_CLEANING     = 16 + JposConst.JPOSERREXT; // (Several)
}