#define I2C_ADDR 0x6B

/* Interrupt 1 */
#define REG_INT1_CTRL 0x0D

/* Who am I - constant value register */
#define REG_WHO_AM_I 0x0F
#define VALUE_WHO_AM_I 0x69

/* Accelerometer data rate */
#define REG_CTRL1_XL 0x10
#define VALUE_CTRL1_XL_ODR_XL_104HZ 0x40

/* Accelerometer high/normal performance mode */
#define REG_CTRL6_C 0x15
#define VALUE_CTRL6_C_XL_HM_MODE_1 0x10

/* Enable accelerometer axies */
#define REG_CTRL9_XL 0x18
#define VALUE_CTRL9_XL_ZEN_XL 0x20
#define VALUE_CTRL9_XL_YEN_XL 0x10
#define VALUE_CTRL9_XL_XEN_XL 0x08

/* Status register */
#define REG_STATUS 0x1E
#define MASK_STATUS_XLDA 0x01

/* Accelerometer axis data */
#define REG_OUTX_L_XL 0x28
#define REG_OUTX_H_XL 0x29
#define REG_OUTY_L_XL 0x2A
#define REG_OUTY_H_XL 0x2B
#define REG_OUTZ_L_XL 0x2C
#define REG_OUTZ_H_XL 0x2D

typedef struct AxisData {
    int reg_low;
    int reg_high;
} AxisData;

#define XL_X_AXIS AxisData {REG_OUTX_L_XL, REG_OUTX_H_XL}
#define XL_Y_AXIS AxisData {REG_OUTY_L_XL, REG_OUTY_H_XL}
#define XL_Z_AXIS AxisData {REG_OUTZ_L_XL, REG_OUTZ_H_XL}
