<h2 align="center">Welcome to the Wiki page of Launchers Mod</h2>

<p align="center">
  <img src="https://user-images.githubusercontent.com/31132987/78308663-58bc0180-7549-11ea-9c09-3a1813e2a986.png">
</p>

## Introduction

Launchers are special blocks that operates similar to a piston but are able to effectively launch
any game entity towards the direction they are facing.

There are various types of launchers that can be build with different recipes.

> [!TIP]
> Apart from the **Ultimate Launcher**, all other types of launchers can be stacked up to 4 times to
> increase their power.

<p align="center">
  <img src="https://user-images.githubusercontent.com/31132987/78309969-ccabd900-754c-11ea-86ed-ae5672b8027f.gif">
</p>

Keep in mind that the first launcher is the _main_ one and it is responsible for setting the base
force multiplier. That basically means that using a Powered Launcher first and then a Normal
Launcher is more effective than using a Normal one and then a Powered one.

Launcher are activated with a redstone pulse and can move only if they are retracted and the block
position in front of them is not occupied or unless it is a tripwire.
Falling on a launcher will prevent the player from taking any damage, even if crouching, this allows
for fast two way elevators.

> [!WARNING]
> It is important to remember that because of the way Minecraft manages blocks borders, falling onto
> the extreme border of the block may cause the player to actually take fall damage.

## Recipes

- _**Base force:**_ it represents the base force applied
- _**Stack power percentage:**_ it represents the percentage of the base force that gets added to
  the stack multiplier when the launcher is stacked
- _**Effective stack multiplier:**_ it represents the effective force added when the launcher is
  stacked (baseForce x stackPowerPercentage)

### Launcher

<div style="display: flex; align-items: center; margin: 20px 0;">
    <img src="https://user-images.githubusercontent.com/31132987/78308522-fc58e200-7548-11ea-9195-2e9faa4cc247.png" alt="Description" style="width: 400px; margin-right: 20px;">
    <div style="flex: 1;">
        <p style="margin: 0;">Base force: <span style="font-weight: bold">1.25</span></p>
        <p style="margin: 0;">Stackable: ✅</p>
        <p style="margin: 0;">Stack power percentage: <span style="font-weight: bold">33.5%</span></p>
        <p style="margin: 0;">Effective stack multiplier: <span style="font-weight: bold">0.41875</span></p>
    </div>
</div>

### Powered Launcher

<div style="display: flex; align-items: center; margin: 20px 0;">
    <img src="https://user-images.githubusercontent.com/31132987/78308525-fcf17880-7548-11ea-815f-bc45d9f1a1ad.png" alt="Description" style="width: 400px; margin-right: 20px;">
    <div style="flex: 1;">
        <p style="margin: 0;">Base force: <span style="font-weight: bold">2.125</span></p>
        <p style="margin: 0;">Stackable: ✅</p>
        <p style="margin: 0;">Stack power percentage: <span style="font-weight: bold">29.75%</span></p>
        <p style="margin: 0;">Effective stack multiplier: <span style="font-weight: bold">0.63218</span></p>
    </div>
</div>

### Extreme Launcher

<div style="display: flex; align-items: center; margin: 20px 0;">
    <img src="https://user-images.githubusercontent.com/31132987/78308521-fbc04b80-7548-11ea-8a37-7ad6fe9a3193.png" alt="Description" style="width: 400px; margin-right: 20px;">
    <div style="flex: 1;">
        <p style="margin: 0;">Base force: <span style="font-weight: bold">2.95</span></p>
        <p style="margin: 0;">Stackable: ✅</p>
        <p style="margin: 0;">Stack power percentage: <span style="font-weight: bold">27.5%</span></p>
        <p style="margin: 0;">Effective stack multiplier: <span style="font-weight: bold">0.81125</span></p>
    </div>
</div>

### Ultimate Launcher

<div style="display: flex; align-items: center; margin: 20px 0;">
    <img src="https://user-images.githubusercontent.com/31132987/78308521-fbc04b80-7548-11ea-8a37-7ad6fe9a3193.png" alt="Description" style="width: 400px; margin-right: 20px;">
    <div style="flex: 1;">
        <p style="margin: 0;">Base force: <span style="font-weight: bold">13.75</span></p>
        <p style="margin: 0;">Stackable: ❌</p>
    </div>
</div>

## Directional Launchers

Launchers can be oriented in any direction. In this case, the force is multiplied by a factor,
increasing the raw power.

> [!NOTE]
> There is no way for directional Launchers to exactly compute the actual distance that can be
> traveled since it depends on the surface and other factors.

Launchers can be activated with tripwires and other redstone contraptions, this allows for fast
items and player trasportation in any direction, as shown in the _.gif_ below.

<p align="center">
  <img src="https://user-images.githubusercontent.com/31132987/103489615-1dbb6700-4e16-11eb-8f11-9e4c910529f6.gif">
</p>

## Force computation

Given the following stack (starting from the top Launcher):

1. Powered Launcher
2. Extreme Launcher
3. Extreme Launcher
4. Launcher

The base force **Fb** is applied by the top launcher, the number one, so $F_b = 2.125$.  
Now, with $S_{\text{tot}}$ as the total stack multiplier starting at 1: $S_{\text{tot}} = 1$, each
stacked launcher (starting from the second) will add its own stack multiplier to $S_{\text{tot}}$:

- Numbers 2 and 3 are Extreme Launchers, so they both add the same multiplier:
  $$S_{\text{tot}} = S_{\text{tot}} + (2 \cdot 0.81125) = 2.6225$$
- Number 4 is a Normal Launcher:
  $$S_{\text{tot}} = S_{\text{tot}} + 0.41875 = 3.04125$$

To obtain the final force $F_{\text{tot}}$, the base force is multiplied by the stack multiplier:  
$$F_{\text{tot}} = F_b \cdot S_{\text{tot}} = 2.125 \cdot 3.04125 = 6.4626$$

## Using the `lcalc` command

The `lcalc` command can be used in order to calculate the force of a given **vertical** Launchers
Stack and hence the estimated height in blocks:

_Parameters_:  
`l : Launcher`  
`p: Powered Launcher`  
`e: Extreme Launcher`
`u: Ultimate Launcher (non stackable)`

**Example**  
`/lcalc e e l p` => calculate the force generated by the following stack:

1. Extreme Launcher
2. Extreme Launcher
3. Launcher
4. Powered Launcher

The following polynomial interpolated curve shows the trend of the height that can be achieved with
a certain force.

![launchers_curve_fit](https://github.com/user-attachments/assets/56d923c9-c31d-4a71-afae-5fad421c75d7)
