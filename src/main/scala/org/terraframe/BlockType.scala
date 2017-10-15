package org.terraframe

object BlockType {
  lazy val allBlocks: List[BlockType] =
    List[BlockType](
      AirBlockType,
      DirtBlockType,
      StoneBlockType,
      CopperOreBlockType,
      IronOreBlockType,
      SilverOreBlockType,
      GoldOreBlockType,
      WoodBlockType,
      WorkbenchBlockType,
      WoodenChestBlockType,
      StoneChestBlockType,
      CopperChestBlockType,
      IronChestBlockType,
      SilverChestBlockType,
      GoldChestBlockType,
      TreeBlockType,
      LeavesBlockType,
      FurnaceBlockType,
      CoalBlockType,
      LumenstoneBlockType,
      WoodenTorchBlockType,
      CoalTorchBlockType,
      LumenstoneTorchBlockType,
      FurnaceOnBlockType,
      WoodenTorchLeftWallBlockType,
      WoodenTorchRightWallBlockType,
      CoalTorchLeftWallBlockType,
      CoalTorchRightWallBlockType,
      LumenstoneTorchLeftWallBlockType,
      LumenstoneTorchRightWallBlockType,
      TreeRootBlockType,
      ZincOreBlockType,
      RhymestoneOreBlockType,
      ObduriteOreBlockType,
      AluminumOreBlockType,
      LeadOreBlockType,
      UraniumOreBlockType,
      ZythiumOreBlockType,
      ZythiumOreOnBlockType,
      SiliconOreBlockType,
      IrradiumOreBlockType,
      NullstoneBlockType,
      MeltstoneBlockType,
      SkystoneBlockType,
      MagnetiteOreBlockType,
      SandBlockType,
      SnowBlockType,
      GlassBlockType,
      SunflowerStage1BlockType,
      SunflowerStage2BlockType,
      SunflowerStage3BlockType,
      MoonflowerStage1BlockType,
      MoonflowerStage2BlockType,
      MoonflowerStage3BlockType,
      DryweedStage1BlockType,
      DryweedStage2BlockType,
      DryweedStage3BlockType,
      GreenleafStage1BlockType,
      GreenleafStage2BlockType,
      GreenleafStage3BlockType,
      FrostleafStage1BlockType,
      FrostleafStage2BlockType,
      FrostleafStage3BlockType,
      CaverootStage1BlockType,
      CaverootStage2BlockType,
      CaverootStage3BlockType,
      SkyblossomStage1BlockType,
      SkyblossomStage2BlockType,
      SkyblossomStage3BlockType,
      VoidRotStage1BlockType,
      VoidRotStage2BlockType,
      VoidRotStage3BlockType,
      GrassBlockType,
      JungleGrassBlockType,
      SwampGrassBlockType,
      MudBlockType,
      SandstoneBlockType,
      MarshleafStage1BlockType,
      MarshleafStage2BlockType,
      MarshleafStage3BlockType,
      ZincChestBlockType,
      RhymestoneChestBlockType,
      ObduriteChestBlockType,
      TreeNoBarkBlockType,
      CobblestoneBlockType,
      ChiseledStoneBlockType,
      ChiseledCobblestoneBlockType,
      StoneBricksBlockType,
      ClayBlockType,
      ClayBricksBlockType,
      VarnishedWoodBlockType,
      DirtTransparentBlockType,
      MagnetiteOreTransparentBlockType,
      GrasstransparentBlockType,
      ZythiumWireBlockType,
      ZythiumWire1PowerBlockType,
      ZythiumWire2PowerBlockType,
      ZythiumWire3PowerBlockType,
      ZythiumWire4PowerBlockType,
      ZythiumWire5PowerBlockType,
      ZythiumTorchBlockType,
      ZythiumTorchLeftWallBlockType,
      ZythiumTorchRightWallBlockType,
      ZythiumLampBlockType,
      ZythiumLampOnBlockType,
      LeverBlockType,
      LeverOnBlockType,
      LeverLeftWallBlockType,
      LeverLeftWallOnBlockType,
      LeverLightWallBlockType,
      LeverRightWallOnBlockType,
      ZythiumAmplifierRightBlockType,
      ZythiumAmplifierDownBlockType,
      ZythiumAmplifierLeftBlockType,
      ZythiumAmplifierUpBlockType,
      ZythiumAmplifierRightOnBlockType,
      ZythiumAmplifierDownOnBlockType,
      ZythiumAmplifierLeftOnBlockType,
      ZythiumAmplifierUpOnBlockType,
      ZythiumInverterRightBlockType,
      ZythiumInverterDownBlockType,
      ZythiumInverterLeftBlockType,
      ZythiumInverterUpBlockType,
      ZythiumInverterRightOnBlockType,
      ZythiumInverterDownOnBlockType,
      ZythiumInverterLeftOnBlockType,
      ZythiumInverterUpOnBlockType,
      ButtonLeftBlockType,
      ButtonLeftOnBlockType,
      ButtonRightBlockType,
      ButtonRightOnBlockType,
      WoodenPressurePlateBlockType,
      WoodenPressurePlateOnBlockType,
      StonePressurePlateBlockType,
      StonePressurePlateOnBlockType,
      ZythiumPressurePlateBlockType,
      ZythiumPressurePlateOnBlockType,
      ZythiumDelayer1DelayRightBlockType,
      ZythiumDelayer1DelayDownBlockType,
      ZythiumDelayer1DelayLeftBlockType,
      ZythiumDelayer1DelayUpBlockType,
      ZythiumDelayer1DelayRightOnBlockType,
      ZythiumDelayer1DelayDownOnBlockType,
      ZythiumDelayer1DelayLeftOnBlockType,
      ZythiumDelayer1DelayUpOnBlockType,
      ZythiumDelayer2DelayRightBlockType,
      ZythiumDelayer2DelayDownBlockType,
      ZythiumDelayer2DelayLeftBlockType,
      ZythiumDelayer2DelayUpBlockType,
      ZythiumDelayer2DelayRightOnBlockType,
      ZythiumDelayer2DelayDownOnBlockType,
      ZythiumDelayer2DelayLeftOnBlockType,
      ZythiumDelayer2DelayUpOnBlockType,
      ZythiumDelayer4DelayRightBlockType,
      ZythiumDelayer4DelayDownBlockType,
      ZythiumDelayer4DelayLeftBlockType,
      ZythiumDelayer4DelayUpBlockType,
      ZythiumDelayer4DelayRightOnBlockType,
      ZythiumDelayer4DelayDownOnBlockType,
      ZythiumDelayer4DelayLeftOnBlockType,
      ZythiumDelayer4DelayUpOnBlockType,
      ZythiumDelayer8DelayRightBlockType,
      ZythiumDelayer8DelayDownBlockType,
      ZythiumDelayer8DelayLeftBlockType,
      ZythiumDelayer8DelayUpBlockType,
      ZythiumDelayer8DelayRightOnBlockType,
      ZythiumDelayer8DelayDownOnBlockType,
      ZythiumDelayer8DelayLeftOnBlockType,
      ZythiumDelayer8DelayUpOnBlockType
    )

  lazy val blocksById = allBlocks.map(b => b.id -> b).toMap

  def lookupById(id: Int): BlockType =
    blocksById.getOrElse(id, AirBlockType)

  def drop(block: BlockType): Option[UiItem] = block match {
    case DirtBlockType                        => Some(DirtUiItem)
    case StoneBlockType                       => Some(StoneUiItem)
    case CopperOreBlockType                   => Some(CopperOreUiItem)
    case IronOreBlockType                     => Some(IronOreUiItem)
    case SilverOreBlockType                   => Some(SilverOreUiItem)
    case GoldOreBlockType                     => Some(GoldOreUiItem)
    case WoodBlockType                        => Some(WoodUiItem)
    case WorkbenchBlockType                   => Some(WorkbenchUiItem)
    case WoodenChestBlockType                 => Some(WoodenChestUiItem)
    case StoneChestBlockType                  => Some(StoneChestUiItem)
    case CopperChestBlockType                 => Some(CopperChestUiItem)
    case IronChestBlockType                   => Some(IronChestUiItem)
    case SilverChestBlockType                 => Some(SilverChestUiItem)
    case GoldChestBlockType                   => Some(GoldChestUiItem)
    case TreeBlockType                        => Some(WoodUiItem)
    case FurnaceBlockType                     => Some(FurnaceUiItem)
    case CoalBlockType                        => Some(CoalUiItem)
    case LumenstoneBlockType                  => Some(LumenstoneUiItem)
    case WoodenTorchBlockType                 => Some(WoodenTorchUiItem)
    case CoalTorchBlockType                   => Some(CoalTorchUiItem)
    case LumenstoneTorchBlockType             => Some(LumenstoneTorchUiItem)
    case FurnaceOnBlockType                   => Some(FurnaceUiItem)
    case WoodenTorchLeftWallBlockType         => Some(WoodenTorchUiItem)
    case WoodenTorchRightWallBlockType        => Some(WoodenTorchUiItem)
    case CoalTorchLeftWallBlockType           => Some(CoalTorchUiItem)
    case CoalTorchRightWallBlockType          => Some(CoalTorchUiItem)
    case LumenstoneTorchLeftWallBlockType     => Some(LumenstoneTorchUiItem)
    case LumenstoneTorchRightWallBlockType    => Some(LumenstoneTorchUiItem)
    case ZincOreBlockType                     => Some(ZincOreUiItem)
    case RhymestoneOreBlockType               => Some(RhymestoneOreUiItem)
    case ObduriteOreBlockType                 => Some(ObduriteOreUiItem)
    case AluminumOreBlockType                 => Some(AluminumOreUiItem)
    case LeadOreBlockType                     => Some(LeadOreUiItem)
    case UraniumOreBlockType                  => Some(UraniumOreUiItem)
    case ZythiumOreBlockType                  => Some(ZythiumOreUiItem)
    case ZythiumOreOnBlockType                => Some(ZythiumOreUiItem)
    case SiliconOreBlockType                  => Some(SiliconOreUiItem)
    case IrradiumOreBlockType                 => Some(IrradiumOreUiItem)
    case NullstoneBlockType                   => Some(NullstoneUiItem)
    case MeltstoneBlockType                   => Some(MeltstoneUiItem)
    case SkystoneBlockType                    => Some(SkystoneUiItem)
    case MagnetiteOreBlockType                => Some(MagnetiteOreUiItem)
    case SandBlockType                        => Some(SandUiItem)
    case SnowBlockType                        => Some(SnowUiItem)
    case SunflowerStage3BlockType             => Some(SunflowerUiItem)
    case MoonflowerStage3BlockType            => Some(MoonflowerUiItem)
    case DryweedStage3BlockType               => Some(DryweedUiItem)
    case GreenleafStage3BlockType             => Some(GreenleafUiItem)
    case FrostleafStage3BlockType             => Some(FrostleafUiItem)
    case CaverootStage3BlockType              => Some(CaverootUiItem)
    case SkyblossomStage3BlockType            => Some(SkyblossomUiItem)
    case VoidRotStage3BlockType               => Some(VoidRotUiItem)
    case GrassBlockType                       => Some(DirtUiItem)
    case JungleGrassBlockType                 => Some(DirtUiItem)
    case SwampGrassBlockType                  => Some(MudUiItem)
    case MudBlockType                         => Some(MudUiItem)
    case SandstoneBlockType                   => Some(SandstoneUiItem)
    case MarshleafStage3BlockType             => Some(MarshleafUiItem)
    case ZincChestBlockType                   => Some(ZincChestUiItem)
    case RhymestoneChestBlockType             => Some(RhymestoneChestUiItem)
    case ObduriteChestBlockType               => Some(ObduriteChestUiItem)
    case TreeNoBarkBlockType                  => Some(WoodUiItem)
    case CobblestoneBlockType                 => Some(CobblestoneUiItem)
    case ChiseledStoneBlockType               => Some(ChiseledStoneUiItem)
    case ChiseledCobblestoneBlockType         => Some(ChiseledCobblestoneUiItem)
    case StoneBricksBlockType                 => Some(StoneBricksUiItem)
    case ClayBlockType                        => Some(ClayUiItem)
    case ClayBricksBlockType                  => Some(ClayBricksUiItem)
    case VarnishedWoodBlockType               => Some(VarnishedWoodUiItem)
    case DirtTransparentBlockType             => Some(DirtUiItem)
    case MagnetiteOreTransparentBlockType     => Some(MagnetiteOreUiItem)
    case GrasstransparentBlockType            => Some(DirtUiItem)
    case ZythiumWireBlockType                 => Some(ZythiumWireUiItem)
    case ZythiumWire1PowerBlockType           => Some(ZythiumWireUiItem)
    case ZythiumWire2PowerBlockType           => Some(ZythiumWireUiItem)
    case ZythiumWire3PowerBlockType           => Some(ZythiumWireUiItem)
    case ZythiumWire4PowerBlockType           => Some(ZythiumWireUiItem)
    case ZythiumWire5PowerBlockType           => Some(ZythiumWireUiItem)
    case ZythiumTorchBlockType                => Some(ZythiumTorchUiItem)
    case ZythiumTorchLeftWallBlockType        => Some(ZythiumTorchUiItem)
    case ZythiumTorchRightWallBlockType       => Some(ZythiumTorchUiItem)
    case ZythiumLampBlockType                 => Some(ZythiumTorchUiItem)
    case ZythiumLampOnBlockType               => Some(ZythiumTorchUiItem)
    case LeverBlockType                       => Some(ZythiumLampUiItem)
    case LeverOnBlockType                     => Some(ZythiumLampUiItem)
    case LeverLeftWallBlockType               => Some(ZythiumLampUiItem)
    case LeverLeftWallOnBlockType             => Some(ZythiumLampUiItem)
    case LeverLightWallBlockType              => Some(ZythiumLampUiItem)
    case LeverRightWallOnBlockType            => Some(ZythiumLampUiItem)
    case ZythiumAmplifierRightBlockType       => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierDownBlockType        => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierLeftBlockType        => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierUpBlockType          => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierRightOnBlockType     => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierDownOnBlockType      => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierLeftOnBlockType      => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierUpOnBlockType        => Some(ZythiumAmplifierUiItem)
    case ZythiumInverterRightBlockType        => Some(ZythiumInverterUiItem)
    case ZythiumInverterDownBlockType         => Some(ZythiumInverterUiItem)
    case ZythiumInverterLeftBlockType         => Some(ZythiumInverterUiItem)
    case ZythiumInverterUpBlockType           => Some(ZythiumInverterUiItem)
    case ZythiumInverterRightOnBlockType      => Some(ZythiumInverterUiItem)
    case ZythiumInverterDownOnBlockType       => Some(ZythiumInverterUiItem)
    case ZythiumInverterLeftOnBlockType       => Some(ZythiumInverterUiItem)
    case ZythiumInverterUpOnBlockType         => Some(ZythiumInverterUiItem)
    case ButtonLeftBlockType                  => Some(ButtonUiItem)
    case ButtonLeftOnBlockType                => Some(ButtonUiItem)
    case ButtonRightBlockType                 => Some(ButtonUiItem)
    case ButtonRightOnBlockType               => Some(ButtonUiItem)
    case WoodenPressurePlateBlockType         => Some(WoodenPressurePlateUiItem)
    case WoodenPressurePlateOnBlockType       => Some(WoodenPressurePlateUiItem)
    case StonePressurePlateBlockType          => Some(StonePressurePlateUiItem)
    case StonePressurePlateOnBlockType        => Some(StonePressurePlateUiItem)
    case ZythiumPressurePlateBlockType        => Some(ZythiumPressurePlateUiItem)
    case ZythiumPressurePlateOnBlockType      => Some(ZythiumPressurePlateUiItem)
    case ZythiumDelayer1DelayRightBlockType   => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayDownBlockType    => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayLeftBlockType    => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayUpBlockType      => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayRightOnBlockType => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayDownOnBlockType  => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayLeftOnBlockType  => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DelayUpOnBlockType    => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer2DelayRightBlockType   => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayDownBlockType    => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayLeftBlockType    => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayUpBlockType      => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayRightOnBlockType => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayDownOnBlockType  => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayLeftOnBlockType  => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DelayUpOnBlockType    => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer4DelayRightBlockType   => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayDownBlockType    => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayLeftBlockType    => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayUpBlockType      => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayRightOnBlockType => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayDownOnBlockType  => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayLeftOnBlockType  => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DelayUpOnBlockType    => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer8DelayRightBlockType   => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayDownBlockType    => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayLeftBlockType    => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayUpBlockType      => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayRightOnBlockType => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayDownOnBlockType  => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayLeftOnBlockType  => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DelayUpOnBlockType    => Some(ZythiumDelayer8UiItem)
    case _                                    => None
  }

  lazy val blockTools: Map[BlockType, Set[UiItem]] = {

    val l1ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      ZincPickUiItem,
      RhymestonePickUiItem,
      ObduritePickUiItem,
      AluminumPickUiItem,
      LeadPickUiItem,
      WoodenPickUiItem,
      StonePickUiItem,
      MagnetitePickUiItem,
      IrradiumPickUiItem
    )
    val l2ToolSet = Set[UiItem](
      CopperAxeUiItem,
      IronAxeUiItem,
      SilverAxeUiItem,
      GoldAxeUiItem,
      ZincAxeUiItem,
      RhymestoneAxeUiItem,
      ObduriteAxeUiItem,
      AluminumAxeUiItem,
      LeadAxeUiItem,
      WoodenAxeUiItem,
      StoneAxeUiItem,
      MagnetiteAxeUiItem,
      IrradiumAxeUiItem
    )
    val l3ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      CopperAxeUiItem,
      IronAxeUiItem,
      SilverAxeUiItem,
      GoldAxeUiItem,
      ZincPickUiItem,
      ZincAxeUiItem,
      RhymestonePickUiItem,
      RhymestoneAxeUiItem,
      ObduritePickUiItem,
      ObduriteAxeUiItem,
      AluminumPickUiItem,
      AluminumAxeUiItem,
      LeadPickUiItem,
      LeadAxeUiItem,
      WoodenPickUiItem,
      WoodenAxeUiItem,
      StonePickUiItem,
      StoneAxeUiItem,
      MagnetitePickUiItem,
      MagnetiteAxeUiItem,
      IrradiumPickUiItem,
      IrradiumAxeUiItem
    )
    val l4ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      ZincPickUiItem,
      RhymestonePickUiItem,
      ObduritePickUiItem,
      AluminumPickUiItem,
      LeadPickUiItem,
      WoodenPickUiItem,
      StonePickUiItem
    )
    val l5ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      CopperAxeUiItem,
      IronAxeUiItem,
      SilverAxeUiItem,
      GoldAxeUiItem,
      ZincPickUiItem,
      ZincAxeUiItem,
      RhymestonePickUiItem,
      RhymestoneAxeUiItem,
      ObduritePickUiItem,
      ObduriteAxeUiItem
    )

    List[(BlockType, Set[UiItem])](
      AirBlockType                         -> Set[UiItem](),
      DirtBlockType                        -> l1ToolSet,
      StoneBlockType                       -> l1ToolSet,
      CopperOreBlockType                   -> l1ToolSet,
      IronOreBlockType                     -> l1ToolSet,
      SilverOreBlockType                   -> l1ToolSet,
      GoldOreBlockType                     -> l1ToolSet,
      WoodBlockType                        -> l2ToolSet,
      WorkbenchBlockType                   -> l2ToolSet,
      WoodenChestBlockType                 -> l2ToolSet,
      StoneChestBlockType                  -> l1ToolSet,
      CopperChestBlockType                 -> l1ToolSet,
      IronChestBlockType                   -> l1ToolSet,
      SilverChestBlockType                 -> l1ToolSet,
      GoldChestBlockType                   -> l1ToolSet,
      TreeBlockType                        -> l2ToolSet,
      FurnaceBlockType                     -> l1ToolSet,
      CoalBlockType                        -> l1ToolSet,
      LumenstoneBlockType                  -> l1ToolSet,
      WoodenTorchBlockType                 -> l3ToolSet,
      CoalTorchBlockType                   -> l3ToolSet,
      LumenstoneTorchBlockType             -> l3ToolSet,
      FurnaceOnBlockType                   -> l1ToolSet,
      WoodenTorchLeftWallBlockType         -> l3ToolSet,
      WoodenTorchRightWallBlockType        -> l3ToolSet,
      CoalTorchLeftWallBlockType           -> l3ToolSet,
      CoalTorchRightWallBlockType          -> l3ToolSet,
      LumenstoneTorchLeftWallBlockType     -> l3ToolSet,
      LumenstoneTorchRightWallBlockType    -> l1ToolSet,
      ZincOreBlockType                     -> l1ToolSet,
      RhymestoneOreBlockType               -> l1ToolSet,
      ObduriteOreBlockType                 -> l1ToolSet,
      AluminumOreBlockType                 -> l1ToolSet,
      LeadOreBlockType                     -> l1ToolSet,
      UraniumOreBlockType                  -> l1ToolSet,
      ZythiumOreBlockType                  -> l1ToolSet,
      ZythiumOreOnBlockType                -> l1ToolSet,
      SiliconOreBlockType                  -> l1ToolSet,
      IrradiumOreBlockType                 -> l1ToolSet,
      NullstoneBlockType                   -> l1ToolSet,
      MeltstoneBlockType                   -> l1ToolSet,
      SkystoneBlockType                    -> l1ToolSet,
      MagnetiteOreBlockType                -> l1ToolSet,
      SandBlockType                        -> l1ToolSet,
      SnowBlockType                        -> l1ToolSet,
      GlassBlockType                       -> l3ToolSet,
      SunflowerStage1BlockType             -> l3ToolSet,
      SunflowerStage2BlockType             -> l3ToolSet,
      SunflowerStage3BlockType             -> l3ToolSet,
      MoonflowerStage1BlockType            -> l3ToolSet,
      MoonflowerStage2BlockType            -> l3ToolSet,
      MoonflowerStage3BlockType            -> l3ToolSet,
      DryweedStage1BlockType               -> l3ToolSet,
      DryweedStage2BlockType               -> l3ToolSet,
      DryweedStage3BlockType               -> l3ToolSet,
      GreenleafStage1BlockType             -> l3ToolSet,
      GreenleafStage2BlockType             -> l3ToolSet,
      GreenleafStage3BlockType             -> l3ToolSet,
      FrostleafStage1BlockType             -> l3ToolSet,
      FrostleafStage2BlockType             -> l3ToolSet,
      FrostleafStage3BlockType             -> l3ToolSet,
      CaverootStage1BlockType              -> l3ToolSet,
      CaverootStage2BlockType              -> l3ToolSet,
      CaverootStage3BlockType              -> l3ToolSet,
      SkyblossomStage1BlockType            -> l3ToolSet,
      SkyblossomStage2BlockType            -> l3ToolSet,
      SkyblossomStage3BlockType            -> l3ToolSet,
      VoidRotStage1BlockType               -> l3ToolSet,
      VoidRotStage2BlockType               -> l3ToolSet,
      VoidRotStage3BlockType               -> l1ToolSet,
      GrassBlockType                       -> l1ToolSet,
      JungleGrassBlockType                 -> l1ToolSet,
      SwampGrassBlockType                  -> l1ToolSet,
      MudBlockType                         -> l1ToolSet,
      SandstoneBlockType                   -> l1ToolSet,
      MarshleafStage1BlockType             -> l3ToolSet,
      MarshleafStage2BlockType             -> l3ToolSet,
      MarshleafStage3BlockType             -> l5ToolSet,
      ZincChestBlockType                   -> l1ToolSet,
      RhymestoneChestBlockType             -> l1ToolSet,
      ObduriteChestBlockType               -> l1ToolSet,
      TreeNoBarkBlockType                  -> l2ToolSet,
      CobblestoneBlockType                 -> l1ToolSet,
      ChiseledStoneBlockType               -> l1ToolSet,
      ChiseledCobblestoneBlockType         -> l1ToolSet,
      StoneBricksBlockType                 -> l1ToolSet,
      ClayBlockType                        -> l1ToolSet,
      ClayBricksBlockType                  -> l1ToolSet,
      VarnishedWoodBlockType               -> l2ToolSet,
      DirtTransparentBlockType             -> l1ToolSet,
      MagnetiteOreTransparentBlockType     -> l1ToolSet,
      GrasstransparentBlockType            -> l4ToolSet,
      ZythiumWireBlockType                 -> l3ToolSet,
      ZythiumWire1PowerBlockType           -> l3ToolSet,
      ZythiumWire2PowerBlockType           -> l3ToolSet,
      ZythiumWire3PowerBlockType           -> l3ToolSet,
      ZythiumWire4PowerBlockType           -> l3ToolSet,
      ZythiumWire5PowerBlockType           -> l3ToolSet,
      ZythiumTorchBlockType                -> l3ToolSet,
      ZythiumTorchLeftWallBlockType        -> l3ToolSet,
      ZythiumTorchRightWallBlockType       -> l3ToolSet,
      ZythiumLampBlockType                 -> l1ToolSet,
      ZythiumLampOnBlockType               -> l1ToolSet,
      LeverBlockType                       -> l1ToolSet,
      LeverOnBlockType                     -> l1ToolSet,
      LeverLeftWallBlockType               -> l1ToolSet,
      LeverLeftWallOnBlockType             -> l1ToolSet,
      LeverLightWallBlockType              -> l1ToolSet,
      LeverRightWallOnBlockType            -> l1ToolSet,
      ZythiumAmplifierRightBlockType       -> l1ToolSet,
      ZythiumAmplifierDownBlockType        -> l1ToolSet,
      ZythiumAmplifierLeftBlockType        -> l1ToolSet,
      ZythiumAmplifierUpBlockType          -> l1ToolSet,
      ZythiumAmplifierRightOnBlockType     -> l1ToolSet,
      ZythiumAmplifierDownOnBlockType      -> l1ToolSet,
      ZythiumAmplifierLeftOnBlockType      -> l1ToolSet,
      ZythiumAmplifierUpOnBlockType        -> l1ToolSet,
      ZythiumInverterRightBlockType        -> l1ToolSet,
      ZythiumInverterDownBlockType         -> l1ToolSet,
      ZythiumInverterLeftBlockType         -> l1ToolSet,
      ZythiumInverterUpBlockType           -> l1ToolSet,
      ZythiumInverterRightOnBlockType      -> l1ToolSet,
      ZythiumInverterDownOnBlockType       -> l1ToolSet,
      ZythiumInverterLeftOnBlockType       -> l1ToolSet,
      ZythiumInverterUpOnBlockType         -> l1ToolSet,
      ButtonLeftBlockType                  -> l1ToolSet,
      ButtonLeftOnBlockType                -> l1ToolSet,
      ButtonRightBlockType                 -> l1ToolSet,
      ButtonRightOnBlockType               -> l1ToolSet,
      WoodenPressurePlateBlockType         -> l2ToolSet,
      WoodenPressurePlateOnBlockType       -> l2ToolSet,
      StonePressurePlateBlockType          -> l1ToolSet,
      StonePressurePlateOnBlockType        -> l1ToolSet,
      ZythiumPressurePlateBlockType        -> l1ToolSet,
      ZythiumPressurePlateOnBlockType      -> l1ToolSet,
      ZythiumDelayer1DelayRightBlockType   -> l1ToolSet,
      ZythiumDelayer1DelayDownBlockType    -> l1ToolSet,
      ZythiumDelayer1DelayLeftBlockType    -> l1ToolSet,
      ZythiumDelayer1DelayUpBlockType      -> l1ToolSet,
      ZythiumDelayer1DelayRightOnBlockType -> l1ToolSet,
      ZythiumDelayer1DelayDownOnBlockType  -> l1ToolSet,
      ZythiumDelayer1DelayLeftOnBlockType  -> l1ToolSet,
      ZythiumDelayer1DelayUpOnBlockType    -> l1ToolSet,
      ZythiumDelayer2DelayRightBlockType   -> l1ToolSet,
      ZythiumDelayer2DelayDownBlockType    -> l1ToolSet,
      ZythiumDelayer2DelayLeftBlockType    -> l1ToolSet,
      ZythiumDelayer2DelayUpBlockType      -> l1ToolSet,
      ZythiumDelayer2DelayRightOnBlockType -> l1ToolSet,
      ZythiumDelayer2DelayDownOnBlockType  -> l1ToolSet,
      ZythiumDelayer2DelayLeftOnBlockType  -> l1ToolSet,
      ZythiumDelayer2DelayUpOnBlockType    -> l1ToolSet,
      ZythiumDelayer4DelayRightBlockType   -> l1ToolSet,
      ZythiumDelayer4DelayDownBlockType    -> l1ToolSet,
      ZythiumDelayer4DelayLeftBlockType    -> l1ToolSet,
      ZythiumDelayer4DelayUpBlockType      -> l1ToolSet,
      ZythiumDelayer4DelayRightOnBlockType -> l1ToolSet,
      ZythiumDelayer4DelayDownOnBlockType  -> l1ToolSet,
      ZythiumDelayer4DelayLeftOnBlockType  -> l1ToolSet,
      ZythiumDelayer4DelayUpOnBlockType    -> l1ToolSet,
      ZythiumDelayer8DelayRightBlockType   -> l1ToolSet,
      ZythiumDelayer8DelayDownBlockType    -> l1ToolSet,
      ZythiumDelayer8DelayLeftBlockType    -> l1ToolSet,
      ZythiumDelayer8DelayUpBlockType      -> l1ToolSet,
      ZythiumDelayer8DelayRightOnBlockType -> l1ToolSet,
      ZythiumDelayer8DelayDownOnBlockType  -> l1ToolSet,
      ZythiumDelayer8DelayLeftOnBlockType  -> l1ToolSet,
      ZythiumDelayer8DelayUpOnBlockType    -> l1ToolSet
    ).toMap
  }

  def blockContainsTool(block: BlockType, uiItem: UiItem): Boolean = {
    blockTools.get(block).exists(_.contains(uiItem))
  }

  def isGsupported(block: BlockType): Boolean = block match {
    case TreeBlockType                   => true
    case TreeNoBarkBlockType             => true
    case WoodenTorchBlockType            => true
    case CoalTorchBlockType              => true
    case LumenstoneTorchBlockType        => true
    case MarshleafStage1BlockType        => true
    case MarshleafStage2BlockType        => true
    case ZythiumTorchBlockType           => true
    case LeverBlockType                  => true
    case LeverOnBlockType                => true
    case WoodenPressurePlateBlockType    => true
    case WoodenPressurePlateOnBlockType  => true
    case StonePressurePlateBlockType     => true
    case StonePressurePlateOnBlockType   => true
    case ZythiumPressurePlateBlockType   => true
    case ZythiumPressurePlateOnBlockType => true
    case SunflowerStage1BlockType        => true
    case SunflowerStage2BlockType        => true
    case SunflowerStage3BlockType        => true
    case MoonflowerStage1BlockType       => true
    case MoonflowerStage2BlockType       => true
    case MoonflowerStage3BlockType       => true
    case DryweedStage1BlockType          => true
    case DryweedStage2BlockType          => true
    case DryweedStage3BlockType          => true
    case GreenleafStage1BlockType        => true
    case GreenleafStage2BlockType        => true
    case GreenleafStage3BlockType        => true
    case FrostleafStage1BlockType        => true
    case FrostleafStage2BlockType        => true
    case FrostleafStage3BlockType        => true
    case CaverootStage1BlockType         => true
    case CaverootStage2BlockType         => true
    case CaverootStage3BlockType         => true
    case SkyblossomStage1BlockType       => true
    case SkyblossomStage2BlockType       => true
    case SkyblossomStage3BlockType       => true
    case VoidRotStage1BlockType          => true
    case VoidRotStage2BlockType          => true
    case VoidRotStage3BlockType          => true
    case GrassBlockType                  => true
    case _                               => false

  }

  def powers(block: BlockType): Boolean = block match {
    case ZythiumTorchBlockType                => true
    case ZythiumTorchLeftWallBlockType        => true
    case ZythiumTorchRightWallBlockType       => true
    case LeverOnBlockType                     => true
    case LeverLeftWallOnBlockType             => true
    case LeverRightWallOnBlockType            => true
    case ZythiumAmplifierRightOnBlockType     => true
    case ZythiumAmplifierDownOnBlockType      => true
    case ZythiumAmplifierLeftOnBlockType      => true
    case ZythiumAmplifierUpOnBlockType        => true
    case ZythiumInverterRightBlockType        => true
    case ZythiumInverterDownBlockType         => true
    case ZythiumInverterLeftBlockType         => true
    case ZythiumInverterUpBlockType           => true
    case ButtonLeftOnBlockType                => true
    case ButtonRightOnBlockType               => true
    case WoodenPressurePlateOnBlockType       => true
    case StonePressurePlateOnBlockType        => true
    case ZythiumPressurePlateOnBlockType      => true
    case ZythiumDelayer1DelayRightOnBlockType => true
    case ZythiumDelayer1DelayDownOnBlockType  => true
    case ZythiumDelayer1DelayLeftOnBlockType  => true
    case ZythiumDelayer1DelayUpOnBlockType    => true
    case ZythiumDelayer2DelayRightOnBlockType => true
    case ZythiumDelayer2DelayDownOnBlockType  => true
    case ZythiumDelayer2DelayLeftOnBlockType  => true
    case ZythiumDelayer2DelayUpOnBlockType    => true
    case ZythiumDelayer4DelayRightOnBlockType => true
    case ZythiumDelayer4DelayDownOnBlockType  => true
    case ZythiumDelayer4DelayLeftOnBlockType  => true
    case ZythiumDelayer4DelayUpOnBlockType    => true
    case ZythiumDelayer8DelayRightOnBlockType => true
    case ZythiumDelayer8DelayDownOnBlockType  => true
    case ZythiumDelayer8DelayLeftOnBlockType  => true
    case ZythiumDelayer8DelayUpOnBlockType    => true
    case _                                    => false
  }
}

sealed abstract class BlockType(val id: Int)
case object AirBlockType                         extends BlockType(0)
case object DirtBlockType                        extends BlockType(1)
case object StoneBlockType                       extends BlockType(2)
case object CopperOreBlockType                   extends BlockType(3)
case object IronOreBlockType                     extends BlockType(4)
case object SilverOreBlockType                   extends BlockType(5)
case object GoldOreBlockType                     extends BlockType(6)
case object WoodBlockType                        extends BlockType(7)
case object WorkbenchBlockType                   extends BlockType(8)
case object WoodenChestBlockType                 extends BlockType(9)
case object StoneChestBlockType                  extends BlockType(10)
case object CopperChestBlockType                 extends BlockType(11)
case object IronChestBlockType                   extends BlockType(12)
case object SilverChestBlockType                 extends BlockType(13)
case object GoldChestBlockType                   extends BlockType(14)
case object TreeBlockType                        extends BlockType(15)
case object LeavesBlockType                      extends BlockType(16)
case object FurnaceBlockType                     extends BlockType(17)
case object CoalBlockType                        extends BlockType(18)
case object LumenstoneBlockType                  extends BlockType(19)
case object WoodenTorchBlockType                 extends BlockType(20)
case object CoalTorchBlockType                   extends BlockType(21)
case object LumenstoneTorchBlockType             extends BlockType(22)
case object FurnaceOnBlockType                   extends BlockType(23)
case object WoodenTorchLeftWallBlockType         extends BlockType(24)
case object WoodenTorchRightWallBlockType        extends BlockType(25)
case object CoalTorchLeftWallBlockType           extends BlockType(26)
case object CoalTorchRightWallBlockType          extends BlockType(27)
case object LumenstoneTorchLeftWallBlockType     extends BlockType(28)
case object LumenstoneTorchRightWallBlockType    extends BlockType(29)
case object TreeRootBlockType                    extends BlockType(30)
case object ZincOreBlockType                     extends BlockType(31)
case object RhymestoneOreBlockType               extends BlockType(32)
case object ObduriteOreBlockType                 extends BlockType(33)
case object AluminumOreBlockType                 extends BlockType(34)
case object LeadOreBlockType                     extends BlockType(35)
case object UraniumOreBlockType                  extends BlockType(36)
case object ZythiumOreBlockType                  extends BlockType(37)
case object ZythiumOreOnBlockType                extends BlockType(38)
case object SiliconOreBlockType                  extends BlockType(39)
case object IrradiumOreBlockType                 extends BlockType(40)
case object NullstoneBlockType                   extends BlockType(41)
case object MeltstoneBlockType                   extends BlockType(42)
case object SkystoneBlockType                    extends BlockType(43)
case object MagnetiteOreBlockType                extends BlockType(44)
case object SandBlockType                        extends BlockType(45)
case object SnowBlockType                        extends BlockType(46)
case object GlassBlockType                       extends BlockType(47)
case object SunflowerStage1BlockType             extends BlockType(48)
case object SunflowerStage2BlockType             extends BlockType(49)
case object SunflowerStage3BlockType             extends BlockType(50)
case object MoonflowerStage1BlockType            extends BlockType(51)
case object MoonflowerStage2BlockType            extends BlockType(52)
case object MoonflowerStage3BlockType            extends BlockType(53)
case object DryweedStage1BlockType               extends BlockType(54)
case object DryweedStage2BlockType               extends BlockType(55)
case object DryweedStage3BlockType               extends BlockType(56)
case object GreenleafStage1BlockType             extends BlockType(57)
case object GreenleafStage2BlockType             extends BlockType(58)
case object GreenleafStage3BlockType             extends BlockType(59)
case object FrostleafStage1BlockType             extends BlockType(60)
case object FrostleafStage2BlockType             extends BlockType(61)
case object FrostleafStage3BlockType             extends BlockType(62)
case object CaverootStage1BlockType              extends BlockType(63)
case object CaverootStage2BlockType              extends BlockType(64)
case object CaverootStage3BlockType              extends BlockType(65)
case object SkyblossomStage1BlockType            extends BlockType(66)
case object SkyblossomStage2BlockType            extends BlockType(67)
case object SkyblossomStage3BlockType            extends BlockType(68)
case object VoidRotStage1BlockType               extends BlockType(69)
case object VoidRotStage2BlockType               extends BlockType(70)
case object VoidRotStage3BlockType               extends BlockType(71)
case object GrassBlockType                       extends BlockType(72)
case object JungleGrassBlockType                 extends BlockType(73)
case object SwampGrassBlockType                  extends BlockType(74)
case object MudBlockType                         extends BlockType(75)
case object SandstoneBlockType                   extends BlockType(76)
case object MarshleafStage1BlockType             extends BlockType(77)
case object MarshleafStage2BlockType             extends BlockType(78)
case object MarshleafStage3BlockType             extends BlockType(79)
case object ZincChestBlockType                   extends BlockType(80)
case object RhymestoneChestBlockType             extends BlockType(81)
case object ObduriteChestBlockType               extends BlockType(82)
case object TreeNoBarkBlockType                  extends BlockType(83)
case object CobblestoneBlockType                 extends BlockType(84)
case object ChiseledStoneBlockType               extends BlockType(85)
case object ChiseledCobblestoneBlockType         extends BlockType(86)
case object StoneBricksBlockType                 extends BlockType(87)
case object ClayBlockType                        extends BlockType(88)
case object ClayBricksBlockType                  extends BlockType(89)
case object VarnishedWoodBlockType               extends BlockType(90)
case object DirtTransparentBlockType             extends BlockType(91)
case object MagnetiteOreTransparentBlockType     extends BlockType(92)
case object GrasstransparentBlockType            extends BlockType(93)
case object ZythiumWireBlockType                 extends BlockType(94)
case object ZythiumWire1PowerBlockType           extends BlockType(95)
case object ZythiumWire2PowerBlockType           extends BlockType(96)
case object ZythiumWire3PowerBlockType           extends BlockType(97)
case object ZythiumWire4PowerBlockType           extends BlockType(98)
case object ZythiumWire5PowerBlockType           extends BlockType(99)
case object ZythiumTorchBlockType                extends BlockType(100)
case object ZythiumTorchLeftWallBlockType        extends BlockType(101)
case object ZythiumTorchRightWallBlockType       extends BlockType(102)
case object ZythiumLampBlockType                 extends BlockType(103)
case object ZythiumLampOnBlockType               extends BlockType(104)
case object LeverBlockType                       extends BlockType(105)
case object LeverOnBlockType                     extends BlockType(106)
case object LeverLeftWallBlockType               extends BlockType(107)
case object LeverLeftWallOnBlockType             extends BlockType(108)
case object LeverLightWallBlockType              extends BlockType(109)
case object LeverRightWallOnBlockType            extends BlockType(110)
case object ZythiumAmplifierRightBlockType       extends BlockType(111)
case object ZythiumAmplifierDownBlockType        extends BlockType(112)
case object ZythiumAmplifierLeftBlockType        extends BlockType(113)
case object ZythiumAmplifierUpBlockType          extends BlockType(114)
case object ZythiumAmplifierRightOnBlockType     extends BlockType(115)
case object ZythiumAmplifierDownOnBlockType      extends BlockType(116)
case object ZythiumAmplifierLeftOnBlockType      extends BlockType(117)
case object ZythiumAmplifierUpOnBlockType        extends BlockType(118)
case object ZythiumInverterRightBlockType        extends BlockType(119)
case object ZythiumInverterDownBlockType         extends BlockType(120)
case object ZythiumInverterLeftBlockType         extends BlockType(121)
case object ZythiumInverterUpBlockType           extends BlockType(122)
case object ZythiumInverterRightOnBlockType      extends BlockType(123)
case object ZythiumInverterDownOnBlockType       extends BlockType(124)
case object ZythiumInverterLeftOnBlockType       extends BlockType(125)
case object ZythiumInverterUpOnBlockType         extends BlockType(126)
case object ButtonLeftBlockType                  extends BlockType(127)
case object ButtonLeftOnBlockType                extends BlockType(128)
case object ButtonRightBlockType                 extends BlockType(129)
case object ButtonRightOnBlockType               extends BlockType(130)
case object WoodenPressurePlateBlockType         extends BlockType(131)
case object WoodenPressurePlateOnBlockType       extends BlockType(132)
case object StonePressurePlateBlockType          extends BlockType(133)
case object StonePressurePlateOnBlockType        extends BlockType(134)
case object ZythiumPressurePlateBlockType        extends BlockType(135)
case object ZythiumPressurePlateOnBlockType      extends BlockType(136)
case object ZythiumDelayer1DelayRightBlockType   extends BlockType(137)
case object ZythiumDelayer1DelayDownBlockType    extends BlockType(138)
case object ZythiumDelayer1DelayLeftBlockType    extends BlockType(139)
case object ZythiumDelayer1DelayUpBlockType      extends BlockType(140)
case object ZythiumDelayer1DelayRightOnBlockType extends BlockType(141)
case object ZythiumDelayer1DelayDownOnBlockType  extends BlockType(142)
case object ZythiumDelayer1DelayLeftOnBlockType  extends BlockType(143)
case object ZythiumDelayer1DelayUpOnBlockType    extends BlockType(144)
case object ZythiumDelayer2DelayRightBlockType   extends BlockType(145)
case object ZythiumDelayer2DelayDownBlockType    extends BlockType(146)
case object ZythiumDelayer2DelayLeftBlockType    extends BlockType(147)
case object ZythiumDelayer2DelayUpBlockType      extends BlockType(148)
case object ZythiumDelayer2DelayRightOnBlockType extends BlockType(149)
case object ZythiumDelayer2DelayDownOnBlockType  extends BlockType(150)
case object ZythiumDelayer2DelayLeftOnBlockType  extends BlockType(151)
case object ZythiumDelayer2DelayUpOnBlockType    extends BlockType(152)
case object ZythiumDelayer4DelayRightBlockType   extends BlockType(153)
case object ZythiumDelayer4DelayDownBlockType    extends BlockType(154)
case object ZythiumDelayer4DelayLeftBlockType    extends BlockType(155)
case object ZythiumDelayer4DelayUpBlockType      extends BlockType(156)
case object ZythiumDelayer4DelayRightOnBlockType extends BlockType(157)
case object ZythiumDelayer4DelayDownOnBlockType  extends BlockType(158)
case object ZythiumDelayer4DelayLeftOnBlockType  extends BlockType(159)
case object ZythiumDelayer4DelayUpOnBlockType    extends BlockType(160)
case object ZythiumDelayer8DelayRightBlockType   extends BlockType(161)
case object ZythiumDelayer8DelayDownBlockType    extends BlockType(162)
case object ZythiumDelayer8DelayLeftBlockType    extends BlockType(163)
case object ZythiumDelayer8DelayUpBlockType      extends BlockType(164)
case object ZythiumDelayer8DelayRightOnBlockType extends BlockType(165)
case object ZythiumDelayer8DelayDownOnBlockType  extends BlockType(166)
case object ZythiumDelayer8DelayLeftOnBlockType  extends BlockType(167)
case object ZythiumDelayer8DelayUpOnBlockType    extends BlockType(168)
