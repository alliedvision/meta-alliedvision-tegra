
S = "${WORKDIR}/git"

SRCREV_nvidia-oot = "928c6b5a82d19fc7d9905104f20920f7fc7cbfb6"
SRCREV_nvgpu = "8a0a5345705e069e398a79dbcba96c5db54a37f1"
SRCREV_nvethernetrm = "1150fc9b2976463a6d839c3515d012b5a27a367b"
SRCREV_hwpm = "d47dc62f4011ffbb0353ba43df5cfb42b967bef2"
SRCREV_nvdisplay = "b2fb5ad0da0c8f24f7e5221b761cc01b4eadaa79"
SRCREV_t23x-public-dts = "85ce314b25a2f112ee4411dca63ecb72cbe508ff"
SRCREV_tegra-public-dts = "8ba5d53ef1e1753f9f2a5b1f7b7b5fc5039de68e"
SRCREV_kernel-devicetree = "19952c8e25702e9de23500c3b1fb351bf4380446"

SRCREV_FORMAT = "nvidia-oot_nvgpu_nvethernetrm_hwpm_nvdisplay"

SRC_URI = "\
    git://github.com/alliedvision/nvidia-oot.git;protocol=https;branch=l4t/l4t-r36.4.3-avt/main;destsuffix=git/nvidia-oot;name=nvidia-oot \
    git://github.com/alliedvision/nvidia-nvgpu.git;protocol=https;branch=l4t/l4t-r36.4.3-avt/main;destsuffix=git/nvgpu;name=nvgpu \
    git://github.com/alliedvision/nvidia-nvethernetrm.git;protocol=https;branch=l4t/l4t-r36.4.3-avt/main;destsuffix=git/nvethernetrm;name=nvethernetrm \
    git://github.com/alliedvision/nvidia-hwpm.git;protocol=https;branch=l4t/l4t-r36.4.3-avt/main;destsuffix=git/hwpm;name=hwpm \
    git://github.com/OE4T/nv-kernel-display-driver.git;protocol=https;branch=l4t/l4t-r36.4.2;destsuffix=git/nvdisplay;name=nvdisplay \
    git://github.com/OE4T/kernel-devicetree.git;protocol=https;branch=l4t/l4t-r36.4.2;destsuffix=git/kernel-devicetree;name=kernel-devicetree \
    git://github.com/OE4T/t23x-public-dts.git;protocol=https;branch=l4t/l4t-r36.4.2;destsuffix=git/hardware/nvidia/t23x/nv-public;name=t23x-public-dts \
    git://github.com/OE4T/tegra-public-dts.git;protocol=https;branch=l4t/l4t-r36.4.2;destsuffix=git/hardware/nvidia/tegra/nv-public;name=tegra-public-dts \
    file://Makefile;subdir=${S} \
    file://0002-Fix-nvdisplay-modules-builds.patch \
    file://0001-tegra-virt-alt-Remove-leading-from-include-path-from.patch \
"

do_configure:prepend() {
    rm -rf ${S}/nvidia-oot/drivers/net/ethernet/nvidia/nvethernet/nvethernetrm/
    ln -s ../../../../../../nvethernetrm ${S}/nvidia-oot/drivers/net/ethernet/nvidia/nvethernet/nvethernetrm
}

require recipes-kernel/nvidia-kernel-oot/nvidia-kernel-oot.inc


do_install() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake MODLIB="${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}" modules_install
    install -d ${D}${includedir}/${BPN}
    ln -s ${BPN} ${D}${includedir}/kernel-module-${BPN}
    find ${B} -name Module.symvers -type f | xargs sed -e's:${B}/::g' >${D}${includedir}/${BPN}/Module.symvers

    cp -R ${S}/nvidia-oot/include/* ${D}/${includedir}/${BPN}
}