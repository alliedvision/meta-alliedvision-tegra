
inherit deploy

DEPENDS:tegra = "kernel-module-nvidia-kernel-oot"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:tegra = " file://0001-Fix-build-configuration-for-tegra-yocto-build.patch "


EXTRA_OEMAKE:append:tegra = " CONFIG_TEGRA_OOT_MODULE=y NVIDIA_KERNEL_OOT_INCLUDE=${RECIPE_SYSROOT}/usr/include/kernel-module-nvidia-kernel-oot "

PACKAGES:append:tegra = " ${PN}-overlays "

do_install:append:tegra() {
    oe_runmake INSTALL_MOD_PATH=${D} overlay_install
}

do_deploy() {
    install -d ${DEPLOYDIR}
    dtbos=$(find ${B}/overlay/*.dtbo)
    for dtbo in ${dtbos}; do 
        install -m 0644 ${dtbo} ${DEPLOYDIR}
    done
}

addtask deploy after do_compile

FILES:${PN}-overlays:tegra = " /boot/*.dtbo "
